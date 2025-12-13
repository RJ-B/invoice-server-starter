package cz.itnetwork.security;

import cz.itnetwork.entity.User;
import cz.itnetwork.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementace rozhraní UserDetailsService používaná Spring Security
 * pro načítání uživatelských údajů během autentizace.
 *
 * Třída slouží jako propojení mezi perzistentní vrstvou (User entity)
 * a bezpečnostním modelem Spring Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Načtení uživatele podle emailové adresy.
     *
     * Metoda je volána Spring Security během procesu autentizace.
     * Pokud uživatel neexistuje, je vyhozena výjimka, která vede
     * k neúspěšnému přihlášení.
     *
     * @param email emailová adresa uživatele sloužící jako přihlašovací jméno
     * @return objekt UserDetails obsahující autentizační a autorizační údaje
     * @throws UsernameNotFoundException pokud uživatel s daným emailem neexistuje
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Vyhledání uživatele podle emailu v databázi
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Uživatel nenalezen"));

        /*
         * Uživatel přihlášený pomocí Google OAuth nemá lokální heslo.
         * Spring Security však vyžaduje neprázdnou hodnotu,
         * proto je v tomto případě použito prázdné heslo.
         */
        String password = user.getPassword() == null ? "" : user.getPassword();

        // Vytvoření objektu UserDetails pro potřeby Spring Security
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(password)
                .roles(user.getRole().name().replace("ROLE_", ""))
                .build();
    }
}
