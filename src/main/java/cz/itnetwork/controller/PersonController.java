package cz.itnetwork.controller;

import cz.itnetwork.dto.Invoice.InvoiceReadDTO;
import cz.itnetwork.dto.Person.PersonDTO;
import cz.itnetwork.dto.Person.PersonStatisticsDTO;
import cz.itnetwork.service.Person.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public List<PersonDTO> getAll() {
        return personService.getAll();
    }

    @GetMapping("/{id}")
    public PersonDTO getById(@PathVariable Integer id) {
        return personService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO create(@RequestBody PersonDTO dto) {
        return personService.create(dto);
    }

    @PutMapping("/{id}")
    public PersonDTO update(@PathVariable Integer id, @RequestBody PersonDTO dto) {
        return personService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        personService.delete(id);
    }

    @GetMapping("/identification/{ico}/sales")
    public List<InvoiceReadDTO> getSalesByICO(@PathVariable String ico) {
        return personService.getSalesByICO(ico);
    }

    @GetMapping("/identification/{ico}/purchases")
    public List<InvoiceReadDTO> getPurchasesByICO(@PathVariable String ico) {
        return personService.getPurchasesByICO(ico);
    }

    @GetMapping("/statistics")
    public List<PersonStatisticsDTO> getPersonStatistics() {
        return personService.getPersonStatistics();
    }


}
