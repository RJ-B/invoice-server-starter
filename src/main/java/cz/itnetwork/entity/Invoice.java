package cz.itnetwork.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDate;

/**
 * Entita reprezentující fakturu uloženou v databázi.
 * Odpovídá tabulce "invoices" a obsahuje perzistentní stav faktury.
 */
@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    /**
     * Primární klíč faktury.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Číslo faktury.
     */
    @NotNull
    @Column(nullable = false)
    private Integer invoiceNumber;

    /**
     * Datum vystavení faktury.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate issued;

    /**
     * Datum splatnosti faktury.
     */
    @NotNull
    @Column(nullable = false)
    private LocalDate dueDate;

    /**
     * Název nebo popis fakturovaného produktu.
     */
    private String product;

    /**
     * Cena faktury bez DPH.
     */
    @NotNull
    @Positive
    @Column(nullable = false)
    private Double price;

    /**
     * Hodnota DPH vztahující se k faktuře.
     */
    @PositiveOrZero
    private Double vat;

    /**
     * Doplňující poznámka k faktuře.
     */
    private String note;

    /**
     * Prodávající osoba (vztah N:1).
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Person seller;

    /**
     * Kupující osoba (vztah N:1).
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private Person buyer;

    /**
     * Příznak logického smazání faktury.
     * Pokud je true, faktura je skrytá a není zobrazována v přehledech.
     */
    private Boolean hidden;
}
