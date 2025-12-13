package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository rozhraní zodpovědné za přístup k datům entity User.
 *
 * Poskytuje metody pro vyhledávání uživatelů podle specifických
 * identifikátorů a slouží výhradně k práci s perzistentními daty.
 * Neobsahuje aplikační ani bezpečnostní logiku.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Ověření existence uživatele podle emailové adresy.
     *
     * @param email emailová adresa uživatele
     * @return true, pokud uživatel s daným emailem existuje, jinak false
     */
    boolean existsByEmail(String email);

    /**
     * Vyhledání uživatele podle emailové adresy.
     *
     * @param email emailová adresa uživatele
     * @return Optional obsahující uživatele, pokud existuje
     */
    Optional<User> findByEmail(String email);

    /**
     * Vyhledání uživatele registrovaného pomocí Google OAuth
     * podle jeho Google identifikátoru.
     *
     * @param googleId unikátní identifikátor uživatele poskytnutý Googlem
     * @return Optional obsahující uživatele, pokud existuje
     */
    Optional<User> findByGoogleId(String googleId);
}
