package cz.itnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String identificationNumber;
    private String taxNumber;
    private String accountNumber;
    private String bankCode;
    private String iban;
    private String telephone;
    private String mail;
    private String street;
    private String zip;
    private String city;

    @Enumerated(EnumType.STRING)
    private Countries country;
    private String note;
    private Boolean hidden;
}
