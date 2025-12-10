package cz.itnetwork.controller;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceStatisticsDTO;
import cz.itnetwork.service.Invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public List<InvoiceDTO> getAll(
            @RequestParam(required = false) String buyerID,
            @RequestParam(required = false) String sellerID,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer limit
    ) {
        return invoiceService.getAll(buyerID, sellerID, product, minPrice, maxPrice, limit);
    }

    @GetMapping("/{id}")
    public InvoiceDTO getById(@PathVariable Integer id) {
        return invoiceService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceDTO create(@RequestBody InvoiceDTO dto) {
        return invoiceService.create(dto);
    }

    @PutMapping("/{id}")
    public InvoiceDTO update(@PathVariable Integer id, @RequestBody InvoiceDTO dto) {
        return invoiceService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        invoiceService.delete(id);
    }

    @GetMapping("/statistics")
    public InvoiceStatisticsDTO getInvoiceStatistics() {
        return invoiceService.getInvoiceStatistics();
    }



}
