package cz.itnetwork.service.Invoice;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceStatisticsDTO;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.Invoice;
import cz.itnetwork.entity.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementace aplikační služby pro práci s fakturami.
 *
 * Třída zajišťuje business logiku související s:
 * - filtrováním faktur (na databázové úrovni),
 * - vytvářením a aktualizací faktur,
 * - logickým mazáním (soft-delete),
 * - získáváním statistických údajů.
 *
 * Slouží jako prostředník mezi controllerem a perzistentní vrstvou.
 */
@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    /**
     * Načtení seznamu faktur na základě zadaných filtračních kritérií.
     *
     * Filtrování probíhá přímo v databázi pomocí optimalizovaného JPQL dotazu
     * (JOIN FETCH), čímž je eliminován N+1 SELECT problém a výrazně
     * zlepšena výkonnost aplikace.
     *
     * @param buyerId  identifikátor kupujícího (volitelný filtr)
     * @param sellerId identifikátor prodávajícího (volitelný filtr)
     * @param product  název nebo část názvu produktu (volitelný filtr)
     * @param minPrice minimální cena faktury (volitelný filtr)
     * @param maxPrice maximální cena faktury (volitelný filtr)
     * @param limit    maximální počet vrácených faktur (volitelné omezení)
     * @return seznam faktur odpovídajících zadaným kritériím
     */
    @Override
    public List<InvoiceDTO> getAll(
            String buyerId,
            String sellerId,
            String product,
            Double minPrice,
            Double maxPrice,
            Integer limit
    ) {

        // Převod ID z URL parametrů na Integer (null = filtr se nepoužije)
        Integer buyer = (buyerId != null && !buyerId.isBlank())
                ? Integer.valueOf(buyerId)
                : null;

        Integer seller = (sellerId != null && !sellerId.isBlank())
                ? Integer.valueOf(sellerId)
                : null;

        // Ošetření prázdného řetězce – prázdný filtr se nepoužije
        String productFilter = (product != null && !product.isBlank())
                ? product
                : null;

        // Databázově optimalizovaný dotaz (JOIN FETCH, žádný N+1 problém)
        List<Invoice> invoices = invoiceRepository.filterInvoices(
                buyer,
                seller,
                productFilter,
                minPrice,
                maxPrice
        );

        // Omezení počtu výsledků + mapování na DTO
        int resultLimit = (limit != null && limit > 0) ? limit : 100;

        return invoices.stream()
                .limit(resultLimit)
                .map(invoiceMapper::toDto)
                .toList();
    }

    /**
     * Načtení detailu faktury podle jejího identifikátoru.
     *
     * @param id identifikátor faktury
     * @return datový přenosový objekt reprezentující fakturu
     */
    @Override
    public InvoiceDTO getById(Integer id) {
        return invoiceMapper.toDto(
                invoiceRepository.findById(id).orElseThrow()
        );
    }

    /**
     * Vytvoření nové faktury.
     *
     * Pokud není příznak hidden explicitně nastaven,
     * je implicitně považován za false.
     *
     * @param dto datový přenosový objekt faktury
     * @return vytvořená faktura
     */
    @Override
    public InvoiceDTO create(InvoiceDTO dto) {

        if (dto.getHidden() == null) {
            dto.setHidden(false);
        }

        Invoice saved = invoiceRepository.save(
                invoiceMapper.toEntity(dto)
        );

        return invoiceMapper.toDto(saved);
    }

    /**
     * Aktualizace existující faktury.
     *
     * Identifikátor faktury je převzat z parametru metody,
     * nikoliv z DTO objektu.
     *
     * @param id  identifikátor aktualizované faktury
     * @param dto datový přenosový objekt s novými údaji faktury
     * @return aktualizovaná faktura
     */
    @Override
    public InvoiceDTO update(Integer id, InvoiceDTO dto) {

        if (dto.getHidden() == null) {
            dto.setHidden(false);
        }

        dto.setId(id);

        Invoice saved = invoiceRepository.save(
                invoiceMapper.toEntity(dto)
        );

        return invoiceMapper.toDto(saved);
    }

    /**
     * Logické smazání faktury.
     *
     * Faktura není odstraněna z databáze,
     * pouze je označena jako skrytá.
     *
     * @param id identifikátor faktury
     */
    @Override
    public void delete(Integer id) {

        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setHidden(true);
        invoiceRepository.save(invoice);
    }

    /**
     * Získání statistických údajů o fakturách.
     *
     * @return objekt obsahující agregované statistiky faktur
     */
    @Override
    public InvoiceStatisticsDTO getInvoiceStatistics() {

        List<Object[]> result = invoiceRepository.getInvoiceStatisticsRaw();

        if (result == null || result.isEmpty()) {
            return InvoiceStatisticsDTO.builder()
                    .currentYearSum(0.0)
                    .allTimeSum(0.0)
                    .invoicesCount(0)
                    .build();
        }

        Object[] row = result.get(0);

        return InvoiceStatisticsDTO.builder()
                .currentYearSum(row[0] != null ? ((Number) row[0]).doubleValue() : 0)
                .allTimeSum(row[1] != null ? ((Number) row[1]).doubleValue() : 0)
                .invoicesCount(row[2] != null ? ((Number) row[2]).intValue() : 0)
                .build();
    }
}
