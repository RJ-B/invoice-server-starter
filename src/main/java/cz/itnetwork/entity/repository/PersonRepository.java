package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository rozhraní zodpovědné za přístup k datům entity Person.
 *
 * Slouží k provádění CRUD operací a specializovaných databázových dotazů.
 * Repository neobsahuje aplikační ani validační logiku a je určeno
 * výhradně k práci s perzistentními daty.
 *
 * Podporuje logické mazání osob pomocí příznaku hidden.
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {

    /**
     * Načtení všech aktivních (viditelných) osob.
     *
     * Osoby označené jako hidden nejsou zahrnuty ve výsledku.
     *
     * @return seznam aktivních osob
     */
    @Query("""
        SELECT p
        FROM Person p
        WHERE p.hidden = false OR p.hidden IS NULL
        ORDER BY p.name
        """)
    List<Person> findAllVisible();

    /**
     * Zjištění existence aktivní osoby podle jejího ID.
     *
     * Slouží zejména k validačním kontrolám při vytváření faktur,
     * aby nebylo možné použít skrytou osobu.
     *
     * @param id identifikátor osoby
     * @return true, pokud existuje aktivní osoba
     */
    boolean existsByIdAndHiddenFalse(Integer id);

    /**
     * Získání statistických údajů o osobách na základě vystavených faktur.
     *
     * JPQL dotaz vrací:
     * - identifikátor osoby,
     * - název osoby,
     * - celkový obrat (součet cen faktur).
     *
     * Statistika zahrnuje i osoby označené jako hidden,
     * aby byla zachována historická konzistence dat.
     *
     * @return seznam objektových polí obsahujících:
     *         [0] identifikátor osoby,
     *         [1] název osoby,
     *         [2] celkový obrat
     */
    @Query("""
        SELECT p.id AS personId,
               p.name AS personName,
               COALESCE(SUM(i.price), 0) AS revenue
        FROM Person p
        LEFT JOIN Invoice i ON i.seller = p
        GROUP BY p.id, p.name
        ORDER BY revenue DESC
        """)
    List<Object[]> getPersonStatisticsRaw();
}
