package cz.itnetwork.dto.Person;

import lombok.Data;

@Data
public class PersonReadDTO {

    private Long id;

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

    private String country;

    private String note;
}
