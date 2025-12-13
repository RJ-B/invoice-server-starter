package cz.itnetwork.dto.Invoice;

import cz.itnetwork.dto.Person.PersonReadDTO;
import lombok.Data;

@Data
public class InvoiceReadDTO {

    private Integer id;

    private int invoiceNumber;

    private PersonReadDTO seller;

    private PersonReadDTO buyer;

    private String issued;

    private String dueDate;

    private String product;

    private double price;

    private double vat;

    private String note;
}
