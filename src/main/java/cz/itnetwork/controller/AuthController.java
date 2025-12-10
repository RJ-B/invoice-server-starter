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
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAuthDTO dto) {

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
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {

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
                        .roles(user.getRole().name().replace("ROLE_", "")) // např. ROLE_USER → USER
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
    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginDTO dto) {

        // 1) Ověření Google ID tokenu
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(dto.getIdToken());
        if (payload == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Neplatný Google token"));
        }

        // 2) Získání údajů z payloadu
        String googleId = payload.getSubject();          // unikátní ID uživatele u Google
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String picture = (String) payload.get("picture");

        // 3) Registrace nebo login uživatele
        User user = userService.registerOrLoginGoogle(googleId, email, name, picture);

        // 4) Vytvořit UserDetails pro JWT
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password("") // Google user nemá lokální heslo
                        .roles(user.getRole().name().replace("ROLE_", ""))
                        .build();

        // 5) Vygenerovat JWT
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
