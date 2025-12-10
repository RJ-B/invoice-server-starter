package cz.itnetwork.service.Person;

import cz.itnetwork.dto.Invoice.InvoiceReadDTO;
import cz.itnetwork.dto.Person.PersonDTO;
import cz.itnetwork.dto.Person.PersonStatisticsDTO;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.Invoice;
import cz.itnetwork.entity.Person;
import cz.itnetwork.dto.mapper.PersonMapper;
import cz.itnetwork.entity.repository.InvoiceRepository;
import cz.itnetwork.entity.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;


    @Override
    public List<PersonDTO> getAll() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toDto)
                .toList();
    }

    @Override
    public PersonDTO getById(Integer id) {
        return personRepository.findById(id)
                .map(personMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    public PersonDTO create(PersonDTO dto) {
        if (dto.getHidden() == null) {
            dto.setHidden(false);
        }
        Person entity = personMapper.toEntity(dto);
        Person saved = personRepository.save(entity);
        return personMapper.toDto(saved);
    }


    @Override
    public PersonDTO update(Integer id, PersonDTO dto) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        Person updated = personMapper.toEntity(dto);
        updated.setId(person.getId());

        return personMapper.toDto(personRepository.save(updated));
    }

    @Override
    public void delete(Integer id) {
        personRepository.deleteById(id);
    }

    @Override
    public List<InvoiceReadDTO> getSalesByICO(String ico) {
        List<Invoice> invoices = invoiceRepository.findBySeller_IdentificationNumber(ico);
        return invoices.stream().map(invoiceMapper::toReadDTO).toList();
    }

    @Override
    public List<InvoiceReadDTO> getPurchasesByICO(String ico) {
        List<Invoice> invoices = invoiceRepository.findByBuyer_IdentificationNumber(ico);
        return invoices.stream().map(invoiceMapper::toReadDTO).toList();
    }

    @Override
    public List<PersonStatisticsDTO> getPersonStatistics() {
        return personRepository.getPersonStatisticsRaw()
                .stream()
                .map(r -> PersonStatisticsDTO.builder()
                        .personId(((Number) r[0]).intValue())
                        .personName((String) r[1])
                        .revenue(((Number) r[2]).doubleValue())
                        .build()
                )
                .toList();
    }

}
