package cz.itnetwork.controller;

import cz.itnetwork.dto.Invoice.InvoiceReadDTO;
import cz.itnetwork.dto.Person.PersonDTO;
import cz.itnetwork.dto.Person.PersonStatisticsDTO;
import cz.itnetwork.service.Person.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller zodpovědný za správu osob (fyzických nebo právnických).
 * Poskytuje endpointy pro CRUD operace, přehled faktur a statistiky.
 */
@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    /**
     * Načtení seznamu všech osob evidovaných v systému.
     *
     * @return seznam osob ve formě DTO
     */
    @GetMapping
    public List<PersonDTO> getAll() {
        return personService.getAll();
    }

    /**
     * Načtení detailu konkrétní osoby podle jejího identifikátoru.
     *
     * @param id identifikátor osoby
     * @return datový přenosový objekt reprezentující osobu
     */
    @GetMapping("/{id}")
    public PersonDTO getById(@PathVariable Integer id) {

        // Validace identifikátoru – ochrana proti neplatným hodnotám
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Neplatné ID osoby");
        }

        return personService.getById(id);
    }

    /**
     * Vytvoření nové osoby.
     *
     * @param dto datový přenosový objekt obsahující údaje o osobě
     * @return vytvořená osoba
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO create(@RequestBody PersonDTO dto) {

        // Základní kontrola vstupního objektu
        if (dto == null) {
            throw new IllegalArgumentException("Osoba nesmí být prázdná");
        }

        return personService.create(dto);
    }

    /**
     * Aktualizace existující osoby.
     *
     * @param id  identifikátor osoby, která má být aktualizována
     * @param dto datový přenosový objekt s novými údaji osoby
     * @return aktualizovaná osoba
     */
    @PutMapping("/{id}")
    public PersonDTO update(@PathVariable Integer id, @RequestBody PersonDTO dto) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Neplatné ID osoby");
        }
        if (dto == null) {
            throw new IllegalArgumentException("Osoba nesmí být prázdná");
        }

        return personService.update(id, dto);
    }

    /**
     * Smazání osoby podle jejího identifikátoru.
     *
     * @param id identifikátor osoby
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Neplatné ID osoby");
        }

        personService.delete(id);
    }

    /**
     * Získání seznamu vystavených faktur na základě identifikačního čísla (IČO).
     *
     * @param ico identifikační číslo osoby
     * @return seznam faktur, kde je osoba v roli prodávajícího
     */
    @GetMapping("/identification/{ico}/sales")
    public List<InvoiceReadDTO> getSalesByICO(@PathVariable String ico) {

        // Základní validace IČO
        if (ico == null || ico.isBlank()) {
            throw new IllegalArgumentException("IČO nesmí být prázdné");
        }

        return personService.getSalesByICO(ico);
    }

    /**
     * Získání seznamu přijatých faktur na základě identifikačního čísla (IČO).
     *
     * @param ico identifikační číslo osoby
     * @return seznam faktur, kde je osoba v roli kupujícího
     */
    @GetMapping("/identification/{ico}/purchases")
    public List<InvoiceReadDTO> getPurchasesByICO(@PathVariable String ico) {

        // Základní validace IČO
        if (ico == null || ico.isBlank()) {
            throw new IllegalArgumentException("IČO nesmí být prázdné");
        }

        return personService.getPurchasesByICO(ico);
    }

    /**
     * Získání statistických údajů o osobách v systému.
     *
     * @return seznam statistických dat o osobách
     */
    @GetMapping("/statistics")
    public List<PersonStatisticsDTO> getPersonStatistics() {
        return personService.getPersonStatistics();
    }

    /**
     * Našeptávání osob podle názvu (autocomplete).
     *
     * Slouží pro rychlé vyhledávání osob při filtrování faktur.
     *
     * @param query část názvu osoby
     * @return seznam odpovídajících osob
     */
    @GetMapping("/search")
    public List<PersonDTO> searchPersons(@RequestParam String query) {

        if (query == null || query.isBlank()) {
            return List.of();
        }

        return personService.searchByName(query.trim());
    }

}
