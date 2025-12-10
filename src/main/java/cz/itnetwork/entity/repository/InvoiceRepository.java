package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>, JpaSpecificationExecutor<Invoice> {

    List<Invoice> findBySeller_IdentificationNumber(String identificationNumber);
    List<Invoice> findByBuyer_IdentificationNumber(String identificationNumber);

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


    @Query(value = """
    SELECT
        COALESCE(SUM(CASE WHEN DATE_PART('year', issued) = DATE_PART('year', CURRENT_DATE) THEN price ELSE 0 END), 0) AS currentYearSum,
        COALESCE(SUM(price), 0) AS allTimeSum,
        COUNT(*) AS invoicesCount
    FROM invoices
    WHERE hidden = false OR hidden IS NULL
    """,
            nativeQuery = true)
    List<Object[]> getInvoiceStatisticsRaw();

        @Query("""
    SELECT i
    FROM Invoice i
    WHERE (i.hidden = false OR i.hidden IS NULL)
      AND (:buyerId IS NULL OR i.buyer.id = :buyerId)
      AND (:sellerId IS NULL OR i.seller.id = :sellerId)
      AND (:product IS NULL OR LOWER(i.product) LIKE LOWER(CONCAT('%', :product, '%')))
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
