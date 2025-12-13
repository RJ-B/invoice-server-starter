package cz.itnetwork.dto.mapper;

import cz.itnetwork.dto.Person.PersonDTO;
import cz.itnetwork.entity.Person;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper zodpovědný za převod mezi entitou Person
 * a odpovídajícím datovým přenosovým objektem (PersonDTO).
 *
 * Mapper slouží výhradně k transformaci dat mezi vrstvami
 * a neobsahuje žádnou aplikační, validační ani bezpečnostní logiku.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {

    /**
     * Převod datového přenosového objektu osoby na entitu.
     *
     * @param source vstupní datový přenosový objekt typu PersonDTO
     * @return entita Person vytvořená z DTO
     */
    Person toEntity(PersonDTO source);

    /**
     * Převod entity osoby na datový přenosový objekt.
     *
     * @param source entita Person
     * @return datový přenosový objekt PersonDTO
     */
    PersonDTO toDto(Person source);
}
