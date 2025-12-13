package cz.itnetwork.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Komponenta zajišťující ověření Google ID tokenu.
 *
 * Slouží k validaci tokenu získaného v rámci Google OAuth2 autentizace
 * a k extrakci uživatelských údajů z jeho payloadu.
 *
 * Konfigurační údaje jsou načítány z externího konfiguračního souboru,
 * nikoliv přímo ze zdrojového kódu.
 */
@Component
public class GoogleTokenVerifier {

    /**
     * Client ID aplikace registrované u Google OAuth2.
     * Hodnota je načítána z externí konfigurace (.env).
     */
    @Value("${google.oauth.client-id}")
    private String clientId;

    /**
     * Ověření platnosti Google ID tokenu.
     *
     * Metoda ověřuje:
     * - kryptografický podpis tokenu,
     * - jeho časovou platnost,
     * - cílovou aplikaci (audience).
     *
     * V případě neplatného tokenu nebo jakékoliv chyby
     * vrací null, což je následně ošetřeno ve vyšší aplikační vrstvě.
     *
     * @param idTokenString řetězec reprezentující Google ID token
     * @return payload tokenu obsahující údaje o uživateli,
     *         nebo null v případě neplatného tokenu
     */
    public GoogleIdToken.Payload verify(String idTokenString) {

        try {
            // Vytvoření verifieru pro ověřování Google ID tokenů
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            )
                    // Nastavení audience – token musí být vydán pro tuto aplikaci
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            // Ověření tokenu vůči Google API
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                return null;
            }

            // Vrácení payloadu s uživatelskými údaji
            return idToken.getPayload();

        } catch (Exception e) {
            /*
             * Jakákoliv výjimka (neplatný token, síťová chyba, chyba formátu)
             * je považována za neúspěšné ověření.
             * Detail chyby není z bezpečnostních důvodů propagován dál.
             */
            return null;
        }
    }
}
