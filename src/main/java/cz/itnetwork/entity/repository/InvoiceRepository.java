package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository rozhraní zodpovědné za přístup k datům entity Invoice.
 *
 * Využívá Spring Data JPA pro základní CRUD operace a
 * JpaSpecificationExecutor pro dynamické filtrování.
 *
 * Repository neobsahuje aplikační ani validační logiku,
 * slouží výhradně k práci s perzistentními daty.
 */
public interface InvoiceRepository
        extends JpaRepository<Invoice, Integer>, JpaSpecificationExecutor<Invoice> {

    /**
     * Vyhledání všech faktur podle identifikačního čísla prodávajícího.
     *
     * @param identificationNumber identifikační číslo (IČO) prodávajícího
     * @return seznam faktur, kde je daná osoba v roli prodávajícího
     */
    List<Invoice> findBySeller_IdentificationNumber(String identificationNumber);

    /**
     * Vyhledání všech faktur podle identifikačního čísla kupujícího.
     *
     * @param identificationNumber identifikační číslo (IČO) kupujícího
     * @return seznam faktur, kde je daná osoba v roli kupujícího
     */
    List<Invoice> findByBuyer_IdentificationNumber(String identificationNumber);

    /**
     * Získání měsíčního obratu faktur ve formě agregovaných dat.
     */
    @Query(value = """
        SELECT TO_CHAR(issued, 'YYYY-MM') AS month,
               SUM(price) AS turnover
        FROM invoices
        WHERE (hidden = false OR hidden IS NULL)
          AND (:sellerId IS NULL OR seller_id = :sellerId)
          AND (:buyerId IS NULL OR buyer_id = :buyerId)
        GROUP BY TO_CHAR(issued, 'YYYY-MM')
        ORDER BY TO_CHAR(issued, 'YYYY-MM')
        """,
            nativeQuery = true)
    List<Object[]> getMonthlyTurnoverRaw(Integer sellerId, Integer buyerId);

    /**
     * Získání základních statistických údajů o fakturách.
     */
    @Query(value = """
        SELECT
            COALESCE(SUM(CASE
                WHEN DATE_PART('year', issued) = DATE_PART('year', CURRENT_DATE)
                THEN price ELSE 0 END), 0) AS currentYearSum,
            COALESCE(SUM(price), 0) AS allTimeSum,
            COUNT(*) AS invoicesCount
        FROM invoices
        WHERE hidden = false OR hidden IS NULL
        """,
            nativeQuery = true)
    List<Object[]> getInvoiceStatisticsRaw();

    /**
     * Filtrování faktur podle zadaných kritérií.
     *
     * Optimalizovaný JPQL dotaz:
     * - filtrování probíhá v databázi
     * - seller a buyer jsou načteni pomocí JOIN FETCH
     * - eliminuje N+1 SELECT problém
     *
     * @param buyerId  identifikátor kupujícího (volitelný filtr)
     * @param sellerId identifikátor prodávajícího (volitelný filtr)
     * @param product  část názvu produktu (volitelný filtr)
     * @param minPrice minimální cena faktury (volitelný filtr)
     * @param maxPrice maximální cena faktury (volitelný filtr)
     * @return seznam faktur odpovídajících zadaným kritériím
     */
    @Query("""
    SELECT i
    FROM Invoice i
    JOIN FETCH i.seller
    JOIN FETCH i.buyer
    WHERE (i.hidden = false OR i.hidden IS NULL)
      AND (:buyerId IS NULL OR i.buyer.id = :buyerId)
      AND (:sellerId IS NULL OR i.seller.id = :sellerId)
      AND (
           :product IS NULL
           OR i.product LIKE CONCAT('%', CAST(:product AS string), '%')
      )
      AND (:minPrice IS NULL OR i.price >= :minPrice)
      AND (:maxPrice IS NULL OR i.price <= :maxPrice)
    ORDER BY i.issued DESC
""")

    List<Invoice> filterInvoices(
            Integer buyerId,
            Integer sellerId,
            String product,
            Double minPrice,
            Double maxPrice
    );
}
