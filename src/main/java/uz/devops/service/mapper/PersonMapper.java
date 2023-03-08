package uz.devops.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;
import uz.devops.domain.Person;
import uz.devops.service.dto.PersonDTO;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {
    @Mapping(target = "father", source = "father", qualifiedByName = "personId")
    @Mapping(target = "mother", source = "mother", qualifiedByName = "personId")
    @Mapping(target = "people", source = "people", qualifiedByName = "personNameSet")
    @Mapping(target = "divorcedPeople", source = "divorcedPeople", qualifiedByName = "personNameSet")
    PersonDTO toDto(Person s);

    @Mapping(target = "removePerson", ignore = true)
    @Mapping(target = "removeDivorcedPeople", ignore = true)
    Person toEntity(PersonDTO personDTO);

    @Named("personId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PersonDTO toDtoPersonId(Person person);

    @Named("personName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PersonDTO toDtoPersonName(Person person);

    @Named("personNameSet")
    default Set<PersonDTO> toDtoPersonNameSet(Set<Person> person) {
        return person.stream().map(this::toDtoPersonName).collect(Collectors.toSet());
    }
}
