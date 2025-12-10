package cz.itnetwork.service.Invoice;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceStatisticsDTO;

import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> getAll(String buyerId, String sellerId, String product, Double minPrice, Double maxPrice, Integer limit);
    InvoiceDTO getById(Integer id);
    InvoiceDTO create(InvoiceDTO dto);
    InvoiceDTO update(Integer id, InvoiceDTO dto);
    void delete(Integer id);
    InvoiceStatisticsDTO getInvoiceStatistics();

}
