package uz.devops.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.devops.domain.*; // for static metamodels
import uz.devops.domain.Person;
import uz.devops.repository.PersonRepository;
import uz.devops.service.criteria.PersonCriteria;
import uz.devops.service.dto.PersonDTO;
import uz.devops.service.mapper.PersonMapper;

/**
 * Service for executing complex queries for {@link Person} entities in the database.
 * The main input is a {@link PersonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PersonDTO} or a {@link Page} of {@link PersonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PersonQueryService extends QueryService<Person> {

    private final Logger log = LoggerFactory.getLogger(PersonQueryService.class);

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    public PersonQueryService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    /**
     * Return a {@link List} of {@link PersonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PersonDTO> findByCriteria(PersonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personMapper.toDto(personRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PersonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PersonDTO> findByCriteria(PersonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.findAll(specification, page).map(personMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PersonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Person> specification = createSpecification(criteria);
        return personRepository.count(specification);
    }

    /**
     * Function to convert {@link PersonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Person> createSpecification(PersonCriteria criteria) {
        Specification<Person> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getId(),
                    Person_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Person_.name));
            }
            if (criteria.getImg() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImg(), Person_.img));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Person_.gender));
            }
            if (criteria.getBorn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBorn(), Person_.born));
            }
            if (criteria.getDeath() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeath(), Person_.death));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), Person_.country));
            }
            if (criteria.getNationality() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNationality(), Person_.nationality));
            }
            if (criteria.getFatherId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFatherId(), root -> root.join(Person_.father, JoinType.LEFT).get(Person_.id))
                    );
            }
            if (criteria.getMotherId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMotherId(), root -> root.join(Person_.mother, JoinType.LEFT).get(Person_.id))
                    );
            }
            if (criteria.getPersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getPersonId(), root -> root.join(Person_.people, JoinType.LEFT).get(Person_.id))
                    );
            }
            if (criteria.getSpouseId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSpouseId(), root -> root.join(Person_.spouses, JoinType.LEFT).get(Person_.id))
                    );
            }
            if (criteria.getDivorcedPeopleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDivorcedPeopleId(),
                            root -> root.join(Person_.divorcedPeople, JoinType.LEFT).get(Person_.id)
                        )
                    );
            }
            if (criteria.getDivorceesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDivorceesId(), root -> root.join(Person_.divorcees, JoinType.LEFT).get(Person_.id))
                    );
            }
        }
        return specification;
    }
}
