package cz.itnetwork.dto.Invoice;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.itnetwork.dto.Person.PersonReadDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDTO {

    @JsonProperty("_id")
    private Integer id;

    private Integer invoiceNumber;
    private String issued;
    private String dueDate;
    private String product;
    private Double price;
    private Double vat;
    private String note;

    private PersonReadDTO seller;
    private PersonReadDTO buyer;

    private Boolean hidden;

}
