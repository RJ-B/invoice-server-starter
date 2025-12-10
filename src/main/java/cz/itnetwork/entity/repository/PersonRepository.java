package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("""
SELECT p.id AS personId, p.name AS personName, 
       COALESCE(SUM(i.price), 0) AS revenue
FROM Person p
LEFT JOIN Invoice i ON i.seller = p
GROUP BY p.id, p.name
ORDER BY revenue DESC
""")
    List<Object[]> getPersonStatisticsRaw();

}
