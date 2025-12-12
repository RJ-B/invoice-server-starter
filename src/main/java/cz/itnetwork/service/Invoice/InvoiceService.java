package cz.itnetwork.service.Invoice;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceStatisticsDTO;
import java.util.List;

public interface InvoiceService {

    /**
     * Vrátí seznam faktur podle filtrů.
     * Filtry mohou být null → ignorují se.
     */
    List<InvoiceDTO> getAll(
            String buyerId,
            String sellerId,
            String product,
            Double minPrice,
            Double maxPrice,
            Integer limit
    );

    /**
     * Vrátí jednu fakturu podle ID.
     */
    InvoiceDTO getById(Integer id);

    /**
     * Vytvoří novou fakturu.
     */
    InvoiceDTO create(InvoiceDTO dto);

    /**
     * Aktualizuje existující fakturu.
     */
    InvoiceDTO update(Integer id, InvoiceDTO dto);

    /**
     * Soft-delete – nastaví hidden=true.
     */
    void delete(Integer id);

    /**
     * Statistiky – počet faktur, součty atd.
     */
    InvoiceStatisticsDTO getInvoiceStatistics();
}
