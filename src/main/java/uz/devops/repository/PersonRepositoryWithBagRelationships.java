package uz.devops.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import uz.devops.domain.Person;

public interface PersonRepositoryWithBagRelationships {
    Optional<Person> fetchBagRelationships(Optional<Person> person);

    List<Person> fetchBagRelationships(List<Person> people);

    Page<Person> fetchBagRelationships(Page<Person> people);
}
