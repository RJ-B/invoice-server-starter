package cz.itnetwork.service.User;

import cz.itnetwork.dto.user.UserAuthDTO;
import cz.itnetwork.entity.Role;
import cz.itnetwork.entity.User;
import cz.itnetwork.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementace aplikační služby pro práci s uživateli.
 *
 * Třída zajišťuje business logiku související s:
 * - lokální registrací uživatelů (email + heslo),
 * - registrací a přihlášením pomocí Google OAuth,
 * - vyhledáváním uživatelů podle emailové adresy.
 *
 * Slouží jako prostředník mezi controllerem a perzistentní vrstvou.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ================================================
    // Lokální registrace (email + heslo)
    // ================================================

    /**
     * Registrace nového uživatele pomocí emailu a hesla.
     *
     * Metoda provádí kontrolu existence uživatele
     * a bezpečné zahashování hesla před jeho uložením.
     *
     * @param dto datový přenosový objekt obsahující registrační údaje uživatele
     * @return uložený uživatel
     * @throws RuntimeException pokud uživatel s daným emailem již existuje
     */
    @Override
    public User register(UserAuthDTO dto) {

        // Kontrola, zda uživatel s daným emailem již existuje
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Uživatel již existuje.");
        }

        // Vytvoření nové entity uživatele
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .role(Role.ROLE_USER)
                .enabled(true)
                .oauthUser(false)
                .build();

        // Uložení uživatele do databáze
        return userRepository.save(user);
    }

    // ================================================
    // Google registrace / login
    // ================================================

    /**
     * Registrace nebo přihlášení uživatele pomocí Google OAuth.
     *
     * Metoda rozlišuje tři scénáře:
     * 1) uživatel s daným Google ID již existuje → přihlášení,
     * 2) existuje uživatel se stejným emailem → propojení Google účtu,
     * 3) uživatel neexistuje → vytvoření nového Google účtu.
     *
     * @param googleId unikátní identifikátor uživatele poskytnutý Googlem
     * @param email    emailová adresa uživatele
     * @param name     celé jméno uživatele z Google profilu
     * @param picture URL profilového obrázku uživatele
     * @return registrovaný nebo přihlášený uživatel
     */
    @Override
    public User registerOrLoginGoogle(String googleId, String email, String name, String picture) {

        // Bezpečné parsování jména (může být null nebo prázdné)
        String fn = null;
        String ln = null;

        if (name != null && !name.isBlank()) {
            String[] parts = name.trim().split(" ", 2);
            fn = parts[0];
            if (parts.length > 1) {
                ln = parts[1];
            }
        }

        // Finální kopie proměnných pro použití v lambdách
        final String firstName = fn;
        final String lastName = ln;

        // 1) Existuje uživatel s daným Google ID? → přihlášení
        return userRepository.findByGoogleId(googleId)
                .orElseGet(() ->

                        // 2) Existuje uživatel se stejným emailem? → propojení Google účtu
                        userRepository.findByEmail(email)
                                .map(existing -> {

                                    existing.setOauthUser(true);
                                    existing.setGoogleId(googleId);
                                    existing.setProfilePicture(picture);

                                    // Doplnění jména, pokud dosud nebylo vyplněno
                                    if (existing.getFirstName() == null && firstName != null) {
                                        existing.setFirstName(firstName);
                                    }
                                    if (existing.getLastName() == null && lastName != null) {
                                        existing.setLastName(lastName);
                                    }

                                    return userRepository.save(existing);
                                })

                                // 3) Uživatel neexistuje → vytvoření nového Google účtu
                                .orElseGet(() -> {
                                    User googleUser = User.builder()
                                            .email(email)
                                            .firstName(firstName)
                                            .lastName(lastName)
                                            .googleId(googleId)
                                            .profilePicture(picture)
                                            .oauthUser(true)
                                            .enabled(true)
                                            .role(Role.ROLE_USER)
                                            .password(null) // Google uživatel nemá lokální heslo
                                            .build();

                                    return userRepository.save(googleUser);
                                })
                );
    }

    // ================================================
    // Vyhledání uživatele podle emailu
    // ================================================

    /**
     * Vyhledání uživatele podle emailové adresy.
     *
     * @param email emailová adresa uživatele
     * @return uživatel nebo null, pokud neexistuje
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
