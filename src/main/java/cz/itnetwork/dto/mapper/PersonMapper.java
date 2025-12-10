package cz.itnetwork.dto.mapper;

import cz.itnetwork.dto.Person.PersonDTO;
import cz.itnetwork.entity.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    Person toEntity(PersonDTO source);

    PersonDTO toDto(Person source);
}

