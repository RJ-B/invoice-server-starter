package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    // Google OAuth – najít uživatele podle Google ID
    Optional<User> findByGoogleId(String googleId);
}
