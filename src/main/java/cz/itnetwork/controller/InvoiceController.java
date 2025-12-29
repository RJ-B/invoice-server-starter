package cz.itnetwork.controller;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceStatisticsDTO;
import cz.itnetwork.service.Invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller zodpovědný za správu faktur.
 * Poskytuje endpointy pro načítání, vytváření, aktualizaci,
 * mazání faktur a získání statistických údajů.
 */
@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    /**
     * Načtení seznamu faktur s možností filtrování.
     *
     * @param buyerName  název kupujícího (volitelný filtr)
     * @param sellerName název prodávajícího (volitelný filtr)
     * @param minPrice   minimální cena faktury (volitelný filtr)
     * @param maxPrice   maximální cena faktury (volitelný filtr)
     * @param limit      maximální počet vrácených záznamů (volitelné omezení)
     * @return seznam faktur odpovídajících zadaným filtrům
     */
    @GetMapping
    public List<InvoiceDTO> getAll(
            @RequestParam(required = false) String buyerName,
            @RequestParam(required = false) String sellerName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer limit
    ) {

        if (minPrice != null && minPrice < 0) {
            throw new IllegalArgumentException("Minimální cena nesmí být záporná");
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new IllegalArgumentException("Maximální cena nesmí být záporná");
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimální cena nesmí být vyšší než maximální cena");
        }

        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException("Limit musí být kladné číslo");
        }

        return invoiceService.getAll(buyerName, sellerName, minPrice, maxPrice, limit);
    }

    @GetMapping("/{id}")
    public InvoiceDTO getById(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Neplatné ID faktury");
        }
        return invoiceService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceDTO create(@RequestBody InvoiceDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Faktura nesmí být prázdná");
        }
        return invoiceService.create(dto);
    }

    @PutMapping("/{id}")
    public InvoiceDTO update(@PathVariable Integer id, @RequestBody InvoiceDTO dto) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Neplatné ID faktury");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Faktura nesmí být prázdná");
        }
        return invoiceService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Neplatné ID faktury");
        }
        invoiceService.delete(id);
    }

    @GetMapping("/statistics")
    public InvoiceStatisticsDTO getInvoiceStatistics() {
        return invoiceService.getInvoiceStatistics();
    }
}
