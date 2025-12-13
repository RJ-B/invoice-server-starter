package cz.itnetwork.service.Person;

import cz.itnetwork.dto.Invoice.InvoiceReadDTO;
import cz.itnetwork.dto.Person.PersonDTO;
import cz.itnetwork.dto.Person.PersonStatisticsDTO;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.dto.mapper.PersonMapper;
import cz.itnetwork.entity.Invoice;
import cz.itnetwork.entity.Person;
import cz.itnetwork.entity.repository.InvoiceRepository;
import cz.itnetwork.entity.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementace aplikační služby pro práci s osobami.
 *
 * Třída zajišťuje business logiku související se správou osob,
 * jejich vazbou na faktury a generováním statistických přehledů.
 *
 * Používá soft delete pomocí příznaku hidden,
 * aby byla zachována referenční integrita historických faktur.
 */
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    /**
     * Načtení seznamu všech viditelných osob evidovaných v systému.
     *
     * Skryté osoby (hidden = true) nejsou vraceny.
     *
     * @return seznam aktivních osob ve formě DTO
     */
    @Override
    public List<PersonDTO> getAll() {
        return personRepository.findAllVisible()
                .stream()
                .map(personMapper::toDto)
                .toList();
    }

    /**
     * Načtení detailu konkrétní osoby podle jejího identifikátoru.
     *
     * Detail osoby lze načíst i v případě,
     * že je osoba označena jako hidden (např. kvůli fakturám).
     *
     * @param id identifikátor osoby
     * @return datový přenosový objekt reprezentující osobu
     */
    @Override
    public PersonDTO getById(Integer id) {
        return personRepository.findById(id)
                .map(personMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    /**
     * Vytvoření nové osoby.
     *
     * Pokud není příznak hidden explicitně nastaven,
     * je automaticky nastaven na false.
     *
     * @param dto datový přenosový objekt osoby
     * @return vytvořená osoba
     */
    @Override
    public PersonDTO create(PersonDTO dto) {

        if (dto.getHidden() == null) {
            dto.setHidden(false);
        }

        Person entity = personMapper.toEntity(dto);
        Person saved = personRepository.save(entity);

        return personMapper.toDto(saved);
    }

    /**
     * Aktualizace existující osoby.
     *
     * Příznak hidden je převzat z původní entity,
     * aby nedošlo k nechtěnému „oživení“ skryté osoby.
     *
     * @param id  identifikátor osoby
     * @param dto nové údaje osoby
     * @return aktualizovaná osoba
     */
    @Override
    public PersonDTO update(Integer id, PersonDTO dto) {

        Person existing = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        Person updated = personMapper.toEntity(dto);
        updated.setId(existing.getId());
        updated.setHidden(existing.getHidden());

        return personMapper.toDto(
                personRepository.save(updated)
        );
    }

    /**
     * Logické odstranění osoby.
     *
     * Místo fyzického smazání je osoba označena jako hidden,
     * aby zůstala dostupná pro existující faktury.
     *
     * @param id identifikátor osoby
     */
    @Override
    public void delete(Integer id) {

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        person.setHidden(true);
        personRepository.save(person);
    }

    /**
     * Získání seznamu vystavených faktur podle IČO prodávající osoby.
     *
     * @param ico identifikační číslo osoby
     * @return seznam vystavených faktur
     */
    @Override
    public List<InvoiceReadDTO> getSalesByICO(String ico) {

        List<Invoice> invoices =
                invoiceRepository.findBySeller_IdentificationNumber(ico);

        return invoices.stream()
                .map(invoiceMapper::toReadDTO)
                .toList();
    }

    /**
     * Získání seznamu přijatých faktur podle IČO kupující osoby.
     *
     * @param ico identifikační číslo osoby
     * @return seznam přijatých faktur
     */
    @Override
    public List<InvoiceReadDTO> getPurchasesByICO(String ico) {

        List<Invoice> invoices =
                invoiceRepository.findByBuyer_IdentificationNumber(ico);

        return invoices.stream()
                .map(invoiceMapper::toReadDTO)
                .toList();
    }

    /**
     * Získání statistických údajů o osobách.
     *
     * Statistika pracuje i se skrytými osobami,
     * aby byly zachovány historické údaje.
     *
     * @return seznam statistických dat o osobách
     */
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
