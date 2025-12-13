package cz.itnetwork.controller;

import cz.itnetwork.dto.user.*;
import cz.itnetwork.entity.Role;
import cz.itnetwork.entity.User;
import cz.itnetwork.entity.repository.UserRepository;
import cz.itnetwork.security.GoogleTokenVerifier;
import cz.itnetwork.security.JwtUtil;
import cz.itnetwork.service.User.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller zajišťující autentizační operace aplikace.
 * Obsahuje endpointy pro registraci, přihlášení a OAuth autentizaci.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserService userService;

    // ============================================
    //   1) KLASICKÁ REGISTRACE
    // ============================================

    /**
     * Registrace nového uživatele pomocí emailu a hesla.
     *
     * @param dto datový přenosový objekt obsahující registrační údaje uživatele
     *            (email, heslo, jméno, příjmení, telefon)
     * @return ResponseEntity s informací o úspěchu registrace
     *         nebo chybovým stavem při neplatných datech či existujícím uživateli
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAuthDTO dto) {

        if (dto == null ||
                dto.getEmail() == null || dto.getEmail().isBlank() ||
                dto.getPassword() == null || dto.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Neplatná vstupní data"));
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Uživatel s tímto emailem už existuje"));
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .enabled(true)
                .role(Role.ROLE_USER)
                .oauthUser(false)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Registrace proběhla úspěšně"));
    }

    // ============================================
    //   2) LOGIN (email + heslo)
    // ============================================

    /**
     * Přihlášení uživatele pomocí emailu a hesla.
     *
     * @param dto datový přenosový objekt obsahující přihlašovací údaje
     *            (email a heslo)
     * @return ResponseEntity obsahující JWT token a základní údaje o uživateli
     *         nebo chybový stav při neplatných přihlašovacích údajích
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {

        if (dto == null ||
                dto.getEmail() == null || dto.getEmail().isBlank() ||
                dto.getPassword() == null || dto.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Neplatná vstupní data"));
        }

        User user = userRepository.findByEmail(dto.getEmail())
                .orElse(null);

        if (user == null || user.getPassword() == null ||
                !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Neplatný email nebo heslo"));
        }

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name().replace("ROLE_", ""))
                        .build();

        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(
                new LoginResponseDTO(
                        token,
                        user.getEmail(),
                        user.getRole().name(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getProfilePicture()
                )
        );
    }

    // ============================================
    //   3) GOOGLE LOGIN / REGISTRACE
    // ============================================

    /**
     * Přihlášení nebo registrace uživatele prostřednictvím Google OAuth 2.0.
     *
     * @param dto datový přenosový objekt obsahující Google ID token
     * @return ResponseEntity s JWT tokenem a údaji o uživateli,
     *         nebo chybový stav při neplatném Google tokenu
     */
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginDTO dto) {

        if (dto == null || dto.getIdToken() == null || dto.getIdToken().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Neplatná vstupní data"));
        }

        GoogleIdToken.Payload payload = googleTokenVerifier.verify(dto.getIdToken());
        if (payload == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Neplatný Google token"));
        }

        String googleId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        User user = userService.registerOrLoginGoogle(googleId, email, name, picture);

        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password("")
                        .roles(user.getRole().name().replace("ROLE_", ""))
                        .build();

        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(
                new LoginResponseDTO(
                        token,
                        user.getEmail(),
                        user.getRole().name(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getProfilePicture()
                )
        );
    }
}
