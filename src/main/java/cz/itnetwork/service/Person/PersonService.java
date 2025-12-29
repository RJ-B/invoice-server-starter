package cz.itnetwork.service.Person;

import cz.itnetwork.dto.Person.PersonDTO;
import cz.itnetwork.dto.Invoice.InvoiceReadDTO;
import cz.itnetwork.dto.Person.PersonStatisticsDTO;

import java.util.List;

/**
 * Rozhraní aplikační služby zodpovědné za práci s osobami.
 *
 * Definuje kontrakt pro business logiku související se správou osob,
 * jejich vazbou na faktury a získáváním statistických údajů.
 *
 * Implementace této služby zajišťuje komunikaci mezi controllerem
 * a perzistentní vrstvou (repository).
 */
public interface PersonService {

    /**
     * Načtení seznamu všech osob evidovaných v systému.
     *
     * @return seznam osob ve formě DTO
     */
    List<PersonDTO> getAll();

    /**
     * Načtení detailu konkrétní osoby podle jejího identifikátoru.
     *
     * @param id identifikátor osoby
     * @return datový přenosový objekt reprezentující osobu
     */
    PersonDTO getById(Integer id);

    /**
     * Vytvoření nové osoby.
     *
     * @param dto datový přenosový objekt obsahující údaje o osobě
     * @return vytvořená osoba
     */
    PersonDTO create(PersonDTO dto);

    /**
     * Aktualizace existující osoby.
     *
     * @param id  identifikátor osoby, která má být aktualizována
     * @param dto datový přenosový objekt s novými údaji osoby
     * @return aktualizovaná osoba
     */
    PersonDTO update(Integer id, PersonDTO dto);

    /**
     * Odstranění osoby podle jejího identifikátoru.
     *
     * @param id identifikátor osoby
     */
    void delete(Integer id);

    /**
     * Získání seznamu vystavených faktur podle identifikačního čísla osoby (IČO).
     *
     * @param ico identifikační číslo prodávající osoby
     * @return seznam faktur, kde je osoba v roli prodávajícího
     */
    List<InvoiceReadDTO> getSalesByICO(String ico);

    /**
     * Získání seznamu přijatých faktur podle identifikačního čísla osoby (IČO).
     *
     * @param ico identifikační číslo kupující osoby
     * @return seznam faktur, kde je osoba v roli kupujícího
     */
    List<InvoiceReadDTO> getPurchasesByICO(String ico);

    /**
     * Získání statistických údajů o osobách v systému.
     *
     * @return seznam statistických dat o osobách
     */
    List<PersonStatisticsDTO> getPersonStatistics();

    List<PersonDTO> searchByName(String query);

}
