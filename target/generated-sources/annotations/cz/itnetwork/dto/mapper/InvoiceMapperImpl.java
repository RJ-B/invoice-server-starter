package cz.itnetwork.dto.mapper;

import cz.itnetwork.dto.Invoice.InvoiceDTO;
import cz.itnetwork.dto.Invoice.InvoiceReadDTO;
import cz.itnetwork.dto.Person.PersonReadDTO;
import cz.itnetwork.entity.Countries;
import cz.itnetwork.entity.Invoice;
import cz.itnetwork.entity.Person;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class InvoiceMapperImpl implements InvoiceMapper {

    @Override
    public Invoice toEntity(InvoiceDTO source) {
        if ( source == null ) {
            return null;
        }

        Invoice.InvoiceBuilder invoice = Invoice.builder();

        invoice.id( source.getId() );
        invoice.invoiceNumber( source.getInvoiceNumber() );
        if ( source.getIssued() != null ) {
            invoice.issued( LocalDate.parse( source.getIssued() ) );
        }
        if ( source.getDueDate() != null ) {
            invoice.dueDate( LocalDate.parse( source.getDueDate() ) );
        }
        invoice.product( source.getProduct() );
        invoice.price( source.getPrice() );
        invoice.vat( source.getVat() );
        invoice.note( source.getNote() );
        invoice.seller( personReadDTOToPerson( source.getSeller() ) );
        invoice.buyer( personReadDTOToPerson( source.getBuyer() ) );
        invoice.hidden( source.getHidden() );

        return invoice.build();
    }

    @Override
    public InvoiceDTO toDto(Invoice source) {
        if ( source == null ) {
            return null;
        }

        InvoiceDTO.InvoiceDTOBuilder invoiceDTO = InvoiceDTO.builder();

        invoiceDTO.id( source.getId() );
        invoiceDTO.invoiceNumber( source.getInvoiceNumber() );
        if ( source.getIssued() != null ) {
            invoiceDTO.issued( DateTimeFormatter.ISO_LOCAL_DATE.format( source.getIssued() ) );
        }
        if ( source.getDueDate() != null ) {
            invoiceDTO.dueDate( DateTimeFormatter.ISO_LOCAL_DATE.format( source.getDueDate() ) );
        }
        invoiceDTO.product( source.getProduct() );
        invoiceDTO.price( source.getPrice() );
        invoiceDTO.vat( source.getVat() );
        invoiceDTO.note( source.getNote() );
        invoiceDTO.seller( personToPersonReadDTO( source.getSeller() ) );
        invoiceDTO.buyer( personToPersonReadDTO( source.getBuyer() ) );
        invoiceDTO.hidden( source.getHidden() );

        return invoiceDTO.build();
    }

    @Override
    public InvoiceReadDTO toReadDTO(Invoice source) {
        if ( source == null ) {
            return null;
        }

        InvoiceReadDTO invoiceReadDTO = new InvoiceReadDTO();

        invoiceReadDTO.setId( source.getId() );
        if ( source.getInvoiceNumber() != null ) {
            invoiceReadDTO.setInvoiceNumber( source.getInvoiceNumber() );
        }
        invoiceReadDTO.setSeller( personToPersonReadDTO( source.getSeller() ) );
        invoiceReadDTO.setBuyer( personToPersonReadDTO( source.getBuyer() ) );
        if ( source.getIssued() != null ) {
            invoiceReadDTO.setIssued( DateTimeFormatter.ISO_LOCAL_DATE.format( source.getIssued() ) );
        }
        if ( source.getDueDate() != null ) {
            invoiceReadDTO.setDueDate( DateTimeFormatter.ISO_LOCAL_DATE.format( source.getDueDate() ) );
        }
        invoiceReadDTO.setProduct( source.getProduct() );
        if ( source.getPrice() != null ) {
            invoiceReadDTO.setPrice( source.getPrice() );
        }
        if ( source.getVat() != null ) {
            invoiceReadDTO.setVat( source.getVat() );
        }
        invoiceReadDTO.setNote( source.getNote() );

        return invoiceReadDTO;
    }

    protected Person personReadDTOToPerson(PersonReadDTO personReadDTO) {
        if ( personReadDTO == null ) {
            return null;
        }

        Person.PersonBuilder person = Person.builder();

        if ( personReadDTO.getId() != null ) {
            person.id( personReadDTO.getId().intValue() );
        }
        person.name( personReadDTO.getName() );
        person.identificationNumber( personReadDTO.getIdentificationNumber() );
        person.taxNumber( personReadDTO.getTaxNumber() );
        person.accountNumber( personReadDTO.getAccountNumber() );
        person.bankCode( personReadDTO.getBankCode() );
        person.iban( personReadDTO.getIban() );
        person.telephone( personReadDTO.getTelephone() );
        person.mail( personReadDTO.getMail() );
        person.street( personReadDTO.getStreet() );
        person.zip( personReadDTO.getZip() );
        person.city( personReadDTO.getCity() );
        if ( personReadDTO.getCountry() != null ) {
            person.country( Enum.valueOf( Countries.class, personReadDTO.getCountry() ) );
        }
        person.note( personReadDTO.getNote() );

        return person.build();
    }

    protected PersonReadDTO personToPersonReadDTO(Person person) {
        if ( person == null ) {
            return null;
        }

        PersonReadDTO personReadDTO = new PersonReadDTO();

        if ( person.getId() != null ) {
            personReadDTO.setId( person.getId().longValue() );
        }
        personReadDTO.setName( person.getName() );
        personReadDTO.setIdentificationNumber( person.getIdentificationNumber() );
        personReadDTO.setTaxNumber( person.getTaxNumber() );
        personReadDTO.setAccountNumber( person.getAccountNumber() );
        personReadDTO.setBankCode( person.getBankCode() );
        personReadDTO.setIban( person.getIban() );
        personReadDTO.setTelephone( person.getTelephone() );
        personReadDTO.setMail( person.getMail() );
        personReadDTO.setStreet( person.getStreet() );
        personReadDTO.setZip( person.getZip() );
        personReadDTO.setCity( person.getCity() );
        if ( person.getCountry() != null ) {
            personReadDTO.setCountry( person.getCountry().name() );
        }
        personReadDTO.setNote( person.getNote() );

        return personReadDTO;
    }
}
