package cz.itnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hlavní spouštěcí třída Spring Boot aplikace.
 *
 * Třída slouží jako vstupní bod aplikace a zajišťuje její inicializaci,
 * automatickou konfiguraci a spuštění aplikačního kontextu Spring Frameworku.
 */
@SpringBootApplication
public class ApplicationMain {

    /**
     * Vstupní bod aplikace.
     *
     * Metoda inicializuje Spring Boot aplikaci a spouští vestavěný
     * aplikační server spolu s konfigurovanými komponentami.
     *
     * @param args argumenty předané při spuštění aplikace
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }
}
