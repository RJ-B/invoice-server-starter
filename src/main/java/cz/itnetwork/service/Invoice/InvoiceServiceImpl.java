package cz.itnetwork.service.Invoice;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceStatisticsDTO;
import cz.itnetwork.entity.Invoice;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public List<InvoiceDTO> getAll(
            String buyerId,
            String sellerId,
            String product,
            Double minPrice,
            Double maxPrice,
            Integer limit
    ) {

        // převod hodnot z URL → BigDecimal
        BigDecimal min = (minPrice != null) ? BigDecimal.valueOf(minPrice) : null;
        BigDecimal max = (maxPrice != null) ? BigDecimal.valueOf(maxPrice) : null;

        return invoiceRepository.findAll().stream()

                // buyer
                .filter(i ->
                        buyerId == null || buyerId.isBlank() ||
                                (i.getBuyer() != null &&
                                        String.valueOf(i.getBuyer().getId()).equals(buyerId))
                )

                // seller
                .filter(i ->
                        sellerId == null || sellerId.isBlank() ||
                                (i.getSeller() != null &&
                                        String.valueOf(i.getSeller().getId()).equals(sellerId))
                )

                // product
                .filter(i ->
                        product == null || product.isBlank() ||
                                (i.getProduct() != null &&
                                        i.getProduct().toLowerCase().contains(product.toLowerCase()))
                )

                // MIN PRICE — plně funkční BigDecimal porovnání
                .filter(i -> {
                    if (min == null) return true;
                    if (i.getPrice() == null) return false;
                    BigDecimal price = BigDecimal.valueOf(i.getPrice());
                    return price.compareTo(min) >= 0;
                })

                // MAX PRICE — plně funkční BigDecimal porovnání
                .filter(i -> {
                    if (max == null) return true;
                    if (i.getPrice() == null) return false;
                    BigDecimal price = BigDecimal.valueOf(i.getPrice());
                    return price.compareTo(max) <= 0;
                })

                // LIMIT
                .limit(limit != null && limit > 0 ? limit : Integer.MAX_VALUE)

                .map(invoiceMapper::toDto)
                .toList();
    }


    @Override
    public InvoiceDTO getById(Integer id) {
        return invoiceMapper.toDto(invoiceRepository.findById(id).orElseThrow());
    }

    @Override
    public InvoiceDTO create(InvoiceDTO dto) {
        if (dto.getHidden() == null) dto.setHidden(false);
        Invoice saved = invoiceRepository.save(invoiceMapper.toEntity(dto));
        return invoiceMapper.toDto(saved);
    }

    @Override
    public InvoiceDTO update(Integer id, InvoiceDTO dto) {
        if (dto.getHidden() == null) dto.setHidden(false);
        dto.setId(id);
        Invoice saved = invoiceRepository.save(invoiceMapper.toEntity(dto));
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
