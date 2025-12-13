package cz.itnetwork.service.Invoice;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceStatisticsDTO;

import java.util.List;

/**
 * Rozhraní aplikační služby zodpovědné za práci s fakturami.
 *
 * Definuje kontrakt pro business logiku související s vytvářením,
 * úpravou, mazáním a získáváním faktur a jejich statistik.
 *
 * Implementace této služby zajišťuje komunikaci mezi controllerem
 * a perzistentní vrstvou (repository).
 */
public interface InvoiceService {

    /**
     * Načtení seznamu faktur na základě zadaných filtračních kritérií.
     *
     * Jednotlivé filtry mohou být null, v takovém případě jsou ignorovány.
     *
     * @param buyerId  identifikátor kupujícího (volitelný filtr)
     * @param sellerId identifikátor prodávajícího (volitelný filtr)
     * @param product  název nebo část názvu produktu (volitelný filtr)
     * @param minPrice minimální cena faktury (volitelný filtr)
     * @param maxPrice maximální cena faktury (volitelný filtr)
     * @param limit    maximální počet vrácených faktur (volitelné omezení)
     * @return seznam faktur odpovídajících zadaným kritériím
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
     * Načtení detailu konkrétní faktury podle jejího identifikátoru.
     *
     * @param id identifikátor faktury
     * @return datový přenosový objekt reprezentující fakturu
     */
    InvoiceDTO getById(Integer id);

    /**
     * Vytvoření nové faktury.
     *
     * @param dto datový přenosový objekt obsahující údaje o faktuře
     * @return vytvořená faktura
     */
    InvoiceDTO create(InvoiceDTO dto);

    /**
     * Aktualizace existující faktury.
     *
     * @param id  identifikátor faktury, která má být aktualizována
     * @param dto datový přenosový objekt s novými údaji faktury
     * @return aktualizovaná faktura
     */
    InvoiceDTO update(Integer id, InvoiceDTO dto);

    /**
     * Logické smazání faktury.
     *
     * Metoda neodstraňuje záznam z databáze,
     * pouze nastaví příznak hidden na hodnotu true.
     *
     * @param id identifikátor faktury
     */
    void delete(Integer id);

    /**
     * Získání statistických údajů o fakturách.
     *
     * @return objekt obsahující agregované statistiky faktur
     */
    InvoiceStatisticsDTO getInvoiceStatistics();
}
