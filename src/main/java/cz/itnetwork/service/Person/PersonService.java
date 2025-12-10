package cz.itnetwork.service.Person;

import cz.itnetwork.dto.Person.PersonDTO;
import cz.itnetwork.dto.Invoice.InvoiceReadDTO;  // ← Tohle chybělo
import cz.itnetwork.dto.Person.PersonStatisticsDTO;

import java.util.List;

public interface PersonService {

    List<PersonDTO> getAll();
    PersonDTO getById(Integer id);
    PersonDTO create(PersonDTO dto);
    PersonDTO update(Integer id, PersonDTO dto);
    void delete(Integer id);

    List<InvoiceReadDTO> getSalesByICO(String ico);
    List<InvoiceReadDTO> getPurchasesByICO(String ico);
    List<PersonStatisticsDTO> getPersonStatistics();

}
