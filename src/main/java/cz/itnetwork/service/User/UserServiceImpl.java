package cz.itnetwork.service.User;

import cz.itnetwork.dto.user.UserAuthDTO;
import cz.itnetwork.entity.Role;
import cz.itnetwork.entity.User;
import cz.itnetwork.entity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ================================================
    // Lokální registrace (email + heslo)
    // ================================================
    @Override
    public User register(UserAuthDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Uživatel již existuje.");
        }

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

        return userRepository.save(user);
    }

    // ================================================
    // Google registrace/login
    // ================================================
    @Override
    public User registerOrLoginGoogle(String googleId, String email, String name, String picture) {

        // 0) bezpečné parsování jména (může být null / prázdné)
        String fn = null;
        String ln = null;

        if (name != null && !name.isBlank()) {
            String[] parts = name.trim().split(" ", 2);
            fn = parts[0];
            if (parts.length > 1) {
                ln = parts[1];
            }
        }

// finální kopie pro lambdy
        final String firstName = fn;
        final String lastName = ln;


        // 1) Existuje uživatel s tímto googleId? → login
        return userRepository.findByGoogleId(googleId)
                .orElseGet(() ->

                        // 2) Už existuje e-mail normálně? → připnout Google ID
                        userRepository.findByEmail(email)
                                .map(existing -> {
                                    existing.setOauthUser(true);
                                    existing.setGoogleId(googleId);
                                    existing.setProfilePicture(picture);

                                    // doplníme jméno, pokud chybí
                                    if (existing.getFirstName() == null && firstName != null) {
                                        existing.setFirstName(firstName);
                                    }
                                    if (existing.getLastName() == null && lastName != null) {
                                        existing.setLastName(lastName);
                                    }

                                    return userRepository.save(existing);
                                })
                                // 3) jinak vytvořit nový Google účet
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
                                            .password(null) // Google user nemá heslo
                                            .build();

                                    return userRepository.save(googleUser);
                                })
                );
    }

    // ================================================
    // Najít uživatele podle emailu
    // ================================================
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
