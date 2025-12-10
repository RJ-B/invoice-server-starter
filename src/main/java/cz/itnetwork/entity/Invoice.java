package cz.itnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer invoiceNumber;
    private LocalDate issued;
    private LocalDate dueDate;
    private String product;
    private Double price;
    private Double vat;
    private String note;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Person seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Person buyer;

    private Boolean hidden;
}
