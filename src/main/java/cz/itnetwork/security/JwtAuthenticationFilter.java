package cz.itnetwork.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT autentizační filtr zpracovávající každý HTTP požadavek.
 *
 * Filtr ověřuje přítomnost a platnost JWT tokenu v hlavičce Authorization
 * a v případě úspěšného ověření nastavuje autentizační kontext
 * pro aktuální požadavek.
 *
 * Veřejné endpointy (např. autentizační) jsou zpracování filtru vyňaty.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Interní metoda filtru, která je volána jednou pro každý HTTP požadavek.
     *
     * @param request     HTTP požadavek klienta
     * @param response    HTTP odpověď serveru
     * @param filterChain řetězec dalších filtrů Spring Security
     * @throws ServletException při chybě zpracování filtru
     * @throws IOException      při chybě vstupně-výstupních operací
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Získání cesty požadavku
        String path = request.getRequestURI();

        // ============================
        // IGNOROVÁNÍ VEŘEJNÝCH ENDPOINTŮ
        // ============================
        /*
         * Autentizační endpointy nejsou chráněny JWT,
         * protože slouží k získání tokenu.
         */
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ============================
        // ZPRACOVÁNÍ JWT TOKENU
        // ============================
        String header = request.getHeader("Authorization");
        String token = null;
        String email = null;

        /*
         * JWT token je očekáván v hlavičce Authorization
         * ve formátu: "Bearer <token>".
         */
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            email = jwtUtil.extractUsername(token);
        }

        /*
         * Autentizace je nastavena pouze v případě,
         * že uživatel ještě není autentizován
         * a token obsahuje platné uživatelské jméno.
         */
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Načtení uživatelských údajů z databáze
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Ověření platnosti tokenu vůči uživatelským údajům
            if (jwtUtil.validateToken(token, userDetails)) {

                // Vytvoření autentizačního objektu
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // Navázání detailů požadavku na autentizaci
                auth.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Uložení autentizace do SecurityContext
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Pokračování ve zpracování požadavku
        filterChain.doFilter(request, response);
    }
}
