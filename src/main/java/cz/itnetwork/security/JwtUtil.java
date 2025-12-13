package cz.itnetwork.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility třída zajišťující práci s JSON Web Tokeny (JWT).
 *
 * Slouží k:
 * - generování JWT tokenů,
 * - extrakci údajů z tokenu,
 * - ověřování platnosti tokenu.
 *
 * Tajný klíč pro podepisování tokenů je načítán z externí konfigurace
 * a není uložen přímo ve zdrojovém kódu aplikace.
 */
@Component
public class JwtUtil {

    /**
     * Tajný klíč pro podepisování JWT tokenů.
     * Hodnota je načítána z konfiguračního souboru (.env).
     */
    @Value("${jwt.secret}")
    private String SECRET;

    /**
     * Doba platnosti JWT tokenu (24 hodin).
     */
    private final long EXPIRATION = 1000 * 60 * 60 * 24;

    /**
     * Vytvoření kryptografického klíče pro podepisování tokenů
     * pomocí algoritmu HMAC SHA-256.
     *
     * @return tajný klíč pro podepisování JWT tokenů
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Získání uživatelského jména (subject) z JWT tokenu.
     *
     * @param token JWT token
     * @return uživatelské jméno uložené v tokenu
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Získání data expirace JWT tokenu.
     *
     * @param token JWT token
     * @return datum expirace tokenu
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Obecná metoda pro extrakci libovolného claimu z JWT tokenu.
     *
     * @param token    JWT token
     * @param resolver funkce určující, který claim má být vrácen
     * @param <T>      typ návratové hodnoty
     * @return hodnota požadovaného claimu
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Extrakce všech claimů z JWT tokenu.
     *
     * @param token JWT token
     * @return objekt Claims obsahující data uložená v tokenu
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Ověření, zda je JWT token expirovaný.
     *
     * @param token JWT token
     * @return true, pokud je token expirovaný, jinak false
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Ověření platnosti JWT tokenu vůči uživatelským údajům.
     *
     * Metoda kontroluje:
     * - shodu uživatelského jména v tokenu,
     * - časovou platnost tokenu.
     *
     * @param token       JWT token
     * @param userDetails uživatelské údaje načtené ze systému
     * @return true, pokud je token platný, jinak false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Vygenerování nového JWT tokenu pro autentizovaného uživatele.
     *
     * @param userDetails uživatelské údaje
     * @return nově vytvořený JWT token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Vytvoření JWT tokenu s danými claimy a subjektem.
     *
     * @param claims  mapa claimů uložených do tokenu
     * @param subject uživatelské jméno (subject tokenu)
     * @return JWT token ve formátu String
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
