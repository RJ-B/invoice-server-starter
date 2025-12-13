package cz.itnetwork.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entita reprezentující osobu (fyzickou nebo právnickou),
 * která může v systému vystupovat jako prodávající nebo kupující.
 *
 * Odpovídá databázové tabulce "persons" a uchovává perzistentní data osoby.
 */
@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    /**
     * Primární klíč osoby.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Název osoby nebo obchodní firma.
     */
    @NotNull
    @Column(nullable = false)
    private String name;

    /**
     * Identifikační číslo osoby (IČO).
     */
    @NotNull
    @Column(nullable = false)
    private String identificationNumber;

    /**
     * Daňové identifikační číslo (DIČ).
     */
    private String taxNumber;

    /**
     * Číslo bankovního účtu.
     */
    private String accountNumber;

    /**
     * Kód banky.
     */
    private String bankCode;

    /**
     * Mezinárodní číslo bankovního účtu (IBAN).
     */
    private String iban;

    /**
     * Telefonní kontakt osoby.
     */
    private String telephone;

    /**
     * Emailová adresa osoby.
     */
    @Email
    private String mail;

    /**
     * Ulice a číslo popisné.
     */
    private String street;

    /**
     * PSČ.
     */
    private String zip;

    /**
     * Město.
     */
    private String city;

    /**
     * Stát osoby.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Countries country;

    /**
     * Doplňující poznámka k osobě.
     */
    private String note;

    /**
     * Příznak logického skrytí osoby.
     * Slouží k deaktivaci záznamu bez jeho fyzického odstranění z databáze.
     */
    private Boolean hidden;
}
