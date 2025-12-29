package cz.itnetwork.service.Invoice;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceStatisticsDTO;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.Invoice;
import cz.itnetwork.entity.Person;
import cz.itnetwork.entity.repository.InvoiceRepository;
import cz.itnetwork.entity.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PersonRepository personRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public List<InvoiceDTO> getAll(
            String buyerName,
            String sellerName,
            Double minPrice,
            Double maxPrice,
            Integer limit
    ) {

        Integer buyerId = null;
        Integer sellerId = null;

        if (buyerName != null && !buyerName.isBlank()) {
            buyerId = personRepository
                    .findFirstByNameIgnoreCaseContaining(buyerName.trim())
                    .map(Person::getId)
                    .orElse(null);
        }

        if (sellerName != null && !sellerName.isBlank()) {
            sellerId = personRepository
                    .findFirstByNameIgnoreCaseContaining(sellerName.trim())
                    .map(Person::getId)
                    .orElse(null);
        }

        List<Invoice> invoices = invoiceRepository.filterInvoices(
                buyerId,
                sellerId,
                minPrice,
                maxPrice
        );

        int resultLimit = (limit != null && limit > 0) ? limit : 100;

        return invoices.stream()
                .limit(resultLimit)
                .map(invoiceMapper::toDto)
                .toList();
    }

    @Override
    public InvoiceDTO getById(Integer id) {
        return invoiceMapper.toDto(
                invoiceRepository.findById(id).orElseThrow()
        );
    }

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

    @Override
    public void delete(Integer id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setHidden(true);
        invoiceRepository.save(invoice);
    }

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
