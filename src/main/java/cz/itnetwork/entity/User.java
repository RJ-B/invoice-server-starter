package cz.itnetwork.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entita reprezentující uživatele systému.
 *
 * Uchovává autentizační a autorizační údaje uživatele,
 * včetně podpory klasického přihlášení a Google OAuth2.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Primární klíč uživatele.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Emailová adresa uživatele.
     * Slouží jako unikátní identifikátor pro přihlášení.
     */
    @NotNull
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Zahashované heslo uživatele.
     * U uživatelů přihlášených pomocí Google OAuth může být null.
     */
    private String password;

    /**
     * Příznak, zda je účet aktivní.
     * Slouží k logické deaktivaci uživatele.
     */
    @NotNull
    @Column(nullable = false)
    private Boolean enabled = true;

    /**
     * Role uživatele v systému.
     * Určuje úroveň oprávnění.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // ========== Osobní údaje ==========

    /**
     * Křestní jméno uživatele.
     */
    private String firstName;

    /**
     * Příjmení uživatele.
     */
    private String lastName;

    /**
     * Telefonní kontakt uživatele.
     */
    private String phone;

    // ========== Google OAuth2 ==========

    /**
     * Příznak, zda byl uživatel vytvořen pomocí Google OAuth2.
     */
    @NotNull
    @Column(nullable = false)
    private boolean oauthUser = false;

    /**
     * Unikátní identifikátor uživatele poskytnutý Googlem.
     */
    private String googleId;

    /**
     * URL profilového obrázku uživatele (z Google účtu).
     */
    private String profilePicture;
}
