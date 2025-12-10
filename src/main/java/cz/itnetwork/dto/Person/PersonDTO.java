package cz.itnetwork.dto.Person;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDTO {

    @JsonProperty("_id")
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
    private String country;
    private String note;

    private Boolean hidden;
}
