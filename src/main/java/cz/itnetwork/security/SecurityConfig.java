package cz.itnetwork.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Konfigurační třída zajišťující nastavení bezpečnosti aplikace.
 *
 * Definuje:
 * - pravidla přístupu k jednotlivým endpointům,
 * - bezstavovou bezpečnostní politiku (JWT),
 * - CORS konfiguraci,
 * - šifrování hesel,
 * - integraci JWT autentizačního filtru.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    /**
     * Konstruktor s injektovaným JWT autentizačním filtrem.
     *
     * @param jwtFilter filtr zodpovědný za zpracování JWT tokenů
     */
    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Konfigurace bezpečnostního řetězce Spring Security.
     *
     * Nastavení zahrnuje:
     * - vypnutí CSRF ochrany (bezstavová REST API),
     * - povolení CORS,
     * - zákaz vytváření HTTP session,
     * - definici veřejných a chráněných endpointů,
     * - registraci JWT filtru do bezpečnostního řetězce.
     *
     * @param http objekt pro konfiguraci HTTP bezpečnosti
     * @return nakonfigurovaný bezpečnostní filtr chain
     * @throws Exception v případě chyby konfigurace
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CSRF ochrana není potřeba u bezstavového REST API
                .csrf(csrf -> csrf.disable())

                // Konfigurace CORS politiky
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Bezstavové řízení relací – JWT místo session
                .sessionManagement(sess ->
                        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Definice autorizačních pravidel
                .authorizeHttpRequests(auth -> auth
                        // Povolení preflight OPTIONS požadavků
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Veřejné autentizační endpointy
                        .requestMatchers("/api/auth/**").permitAll()

                        // Vše ostatní vyžaduje autentizaci
                        .anyRequest().authenticated()
                )

                // Registrace JWT filtru před standardním autentizačním filtrem
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Konfigurace CORS (Cross-Origin Resource Sharing).
     *
     * Umožňuje komunikaci frontendové aplikace s backendem.
     * Konfigurace je v této podobě otevřená a může být v produkci
     * zpřísněna na konkrétní domény.
     *
     * @return zdroj CORS konfigurace
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Povolení přenosu autentizačních údajů (např. Authorization header)
        config.setAllowCredentials(true);

        /*
         * Povolení všech originů – vhodné pro vývoj.
         * V produkčním prostředí je doporučeno omezit
         * pouze na konkrétní frontendovou doménu.
         */
        config.addAllowedOriginPattern("*");

        // Povolení všech hlaviček a HTTP metod
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        // Zpřístupnění hlavičky Authorization klientovi
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /**
     * Encoder pro bezpečné ukládání hesel uživatelů.
     *
     * Používá adaptivní hashovací algoritmus BCrypt,
     * který je odolný vůči brute-force útokům.
     *
     * @return instance PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Poskytuje AuthenticationManager používaný Spring Security
     * při autentizaci uživatele.
     *
     * @param config konfigurační objekt autentizace
     * @return instance AuthenticationManager
     * @throws Exception v případě chyby konfigurace
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
