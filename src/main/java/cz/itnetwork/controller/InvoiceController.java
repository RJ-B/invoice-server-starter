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
     * @param buyerID  identifikátor kupujícího (volitelný filtr)
     * @param sellerID identifikátor prodávajícího (volitelný filtr)
     * @param product  název produktu obsaženého ve faktuře (volitelný filtr)
     * @param minPrice minimální cena faktury (volitelný filtr)
     * @param maxPrice maximální cena faktury (volitelný filtr)
     * @param limit    maximální počet vrácených záznamů (volitelné omezení)
     * @return seznam faktur odpovídajících zadaným filtrům
     */
    @GetMapping
    public List<InvoiceDTO> getAll(
            @RequestParam(required = false) String buyerID,
            @RequestParam(required = false) String sellerID,
            @RequestParam(required = false) String product,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer limit
    ) {

        // Základní validace číselných filtrů – ochrana proti neplatným rozsahům
        if (minPrice != null && minPrice < 0) {
            throw new IllegalArgumentException("Minimální cena nesmí být záporná");
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new IllegalArgumentException("Maximální cena nesmí být záporná");
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimální cena nesmí být vyšší než maximální cena");
        }

        // Ochrana proti neplatnému limitu
        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException("Limit musí být kladné číslo");
        }

        return invoiceService.getAll(buyerID, sellerID, product, minPrice, maxPrice, limit);
    }

    /**
     * Načtení detailu konkrétní faktury podle jejího identifikátoru.
     *
     * @param id identifikátor faktury
     * @return datový přenosový objekt reprezentující fakturu
     */
    @GetMapping("/{id}")
    public InvoiceDTO getById(@PathVariable Integer id) {

        // Validace identifikátoru – ochrana proti neplatným hodnotám
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Neplatné ID faktury");
        }

        return invoiceService.getById(id);
    }

    /**
     * Vytvoření nové faktury.
     *
     * @param dto datový přenosový objekt obsahující údaje o faktuře
     * @return vytvořená faktura
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceDTO create(@RequestBody InvoiceDTO dto) {

        // Základní kontrola vstupního objektu
        if (dto == null) {
            throw new IllegalArgumentException("Faktura nesmí být prázdná");
        }

        return invoiceService.create(dto);
    }

    /**
     * Aktualizace existující faktury.
     *
     * @param id  identifikátor faktury, která má být aktualizována
     * @param dto datový přenosový objekt s novými údaji faktury
     * @return aktualizovaná faktura
     */
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

    /**
     * Smazání faktury podle jejího identifikátoru.
     *
     * @param id identifikátor faktury
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Neplatné ID faktury");
        }

        invoiceService.delete(id);
    }

    /**
     * Získání statistických údajů o fakturách.
     *
     * @return objekt obsahující statistiky faktur
     */
    @GetMapping("/statistics")
    public InvoiceStatisticsDTO getInvoiceStatistics() {
        return invoiceService.getInvoiceStatistics();
    }
}
