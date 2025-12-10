package cz.itnetwork.dto.mapper;

import cz.itnetwork.dto.Person.PersonDTO;
import cz.itnetwork.entity.Countries;
import cz.itnetwork.entity.Person;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class PersonMapperImpl implements PersonMapper {

    @Override
    public Person toEntity(PersonDTO source) {
        if ( source == null ) {
            return null;
        }

        Person.PersonBuilder person = Person.builder();

        person.id( source.getId() );
        person.name( source.getName() );
        person.identificationNumber( source.getIdentificationNumber() );
        person.taxNumber( source.getTaxNumber() );
        person.accountNumber( source.getAccountNumber() );
        person.bankCode( source.getBankCode() );
        person.iban( source.getIban() );
        person.telephone( source.getTelephone() );
        person.mail( source.getMail() );
        person.street( source.getStreet() );
        person.zip( source.getZip() );
        person.city( source.getCity() );
        if ( source.getCountry() != null ) {
            person.country( Enum.valueOf( Countries.class, source.getCountry() ) );
        }
        person.note( source.getNote() );
        person.hidden( source.getHidden() );

        return person.build();
    }

    @Override
    public PersonDTO toDto(Person source) {
        if ( source == null ) {
            return null;
        }

        PersonDTO.PersonDTOBuilder personDTO = PersonDTO.builder();

        personDTO.id( source.getId() );
        personDTO.name( source.getName() );
        personDTO.identificationNumber( source.getIdentificationNumber() );
        personDTO.taxNumber( source.getTaxNumber() );
        personDTO.accountNumber( source.getAccountNumber() );
        personDTO.bankCode( source.getBankCode() );
        personDTO.iban( source.getIban() );
        personDTO.telephone( source.getTelephone() );
        personDTO.mail( source.getMail() );
        personDTO.street( source.getStreet() );
        personDTO.zip( source.getZip() );
        personDTO.city( source.getCity() );
        if ( source.getCountry() != null ) {
            personDTO.country( source.getCountry().name() );
        }
        personDTO.note( source.getNote() );
        personDTO.hidden( source.getHidden() );

        return personDTO.build();
    }
}
