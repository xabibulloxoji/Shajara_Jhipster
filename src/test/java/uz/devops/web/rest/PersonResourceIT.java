package uz.devops.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.IntegrationTest;
import uz.devops.domain.Person;
import uz.devops.domain.enumeration.Gender;
import uz.devops.repository.PersonRepository;
import uz.devops.service.PersonService;
import uz.devops.service.dto.PersonDTO;
import uz.devops.service.mapper.PersonMapper;

/**
 * Integration tests for the {@link PersonResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PersonResourceIT {

    private static final String DEFAULT_DIVORCED = "AAAAAAAAAA";
    private static final String UPDATED_DIVORCED = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IMG = "AAAAAAAAAA";
    private static final String UPDATED_IMG = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Instant DEFAULT_BORN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BORN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DEATH = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DEATH = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * java.lang.Integer.MAX_VALUE));

    @Autowired
    private PersonRepository personRepository;

    @Mock
    private PersonRepository personRepositoryMock;

    @Autowired
    private PersonMapper personMapper;

    @Mock
    private PersonService personServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonMockMvc;

    private Person person;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createEntity(EntityManager em) {
        Person person = new Person()
            .divorced(DEFAULT_DIVORCED)
            .name(DEFAULT_NAME)
            .img(DEFAULT_IMG)
            .gender(DEFAULT_GENDER)
            .born(DEFAULT_BORN)
            .death(DEFAULT_DEATH)
            .country(DEFAULT_COUNTRY)
            .nationality(DEFAULT_NATIONALITY);
        return person;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Person createUpdatedEntity(EntityManager em) {
        Person person = new Person()
            .divorced(UPDATED_DIVORCED)
            .name(UPDATED_NAME)
            .img(UPDATED_IMG)
            .gender(UPDATED_GENDER)
            .born(UPDATED_BORN)
            .death(UPDATED_DEATH)
            .country(UPDATED_COUNTRY)
            .nationality(UPDATED_NATIONALITY);
        return person;
    }

    @BeforeEach
    public void initTest() {
        person = createEntity(em);
    }

    @Test
    @Transactional
    void createPerson() throws Exception {
        int databaseSizeBeforeCreate = personRepository.findAll().size();
        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);
        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isCreated());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate + 1);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getDivorced()).isEqualTo(DEFAULT_DIVORCED);
        assertThat(testPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPerson.getImg()).isEqualTo(DEFAULT_IMG);
        assertThat(testPerson.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPerson.getBorn()).isEqualTo(DEFAULT_BORN);
        assertThat(testPerson.getDeath()).isEqualTo(DEFAULT_DEATH);
        assertThat(testPerson.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testPerson.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
    }

    @Test
    @Transactional
    void createPersonWithExistingId() throws Exception {
        // Create the Person with an existing ID
        person.setId(1L);
        PersonDTO personDTO = personMapper.toDto(person);

        int databaseSizeBeforeCreate = personRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPeople() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].divorced").value(hasItem(DEFAULT_DIVORCED)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].born").value(hasItem(DEFAULT_BORN.toString())))
            .andExpect(jsonPath("$.[*].death").value(hasItem(DEFAULT_DEATH.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeopleWithEagerRelationshipsIsEnabled() throws Exception {
        when(personServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(personServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPeopleWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(personServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(personRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get the person
        restPersonMockMvc
            .perform(get(ENTITY_API_URL_ID, person.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(person.getId().intValue()))
            .andExpect(jsonPath("$.divorced").value(DEFAULT_DIVORCED))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.img").value(DEFAULT_IMG))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.born").value(DEFAULT_BORN.toString()))
            .andExpect(jsonPath("$.death").value(DEFAULT_DEATH.toString()))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY));
    }

    @Test
    @Transactional
    void getPeopleByIdFiltering() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        Long id = person.getId();

        defaultPersonShouldBeFound("id.equals=" + id);
        defaultPersonShouldNotBeFound("id.notEquals=" + id);

        defaultPersonShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.greaterThan=" + id);

        defaultPersonShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPersonShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPeopleByDivorcedIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where divorced equals to DEFAULT_DIVORCED
        defaultPersonShouldBeFound("divorced.equals=" + DEFAULT_DIVORCED);

        // Get all the personList where divorced equals to UPDATED_DIVORCED
        defaultPersonShouldNotBeFound("divorced.equals=" + UPDATED_DIVORCED);
    }

    @Test
    @Transactional
    void getAllPeopleByDivorcedIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where divorced in DEFAULT_DIVORCED or UPDATED_DIVORCED
        defaultPersonShouldBeFound("divorced.in=" + DEFAULT_DIVORCED + "," + UPDATED_DIVORCED);

        // Get all the personList where divorced equals to UPDATED_DIVORCED
        defaultPersonShouldNotBeFound("divorced.in=" + UPDATED_DIVORCED);
    }

    @Test
    @Transactional
    void getAllPeopleByDivorcedIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where divorced is not null
        defaultPersonShouldBeFound("divorced.specified=true");

        // Get all the personList where divorced is null
        defaultPersonShouldNotBeFound("divorced.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByDivorcedContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where divorced contains DEFAULT_DIVORCED
        defaultPersonShouldBeFound("divorced.contains=" + DEFAULT_DIVORCED);

        // Get all the personList where divorced contains UPDATED_DIVORCED
        defaultPersonShouldNotBeFound("divorced.contains=" + UPDATED_DIVORCED);
    }

    @Test
    @Transactional
    void getAllPeopleByDivorcedNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where divorced does not contain DEFAULT_DIVORCED
        defaultPersonShouldNotBeFound("divorced.doesNotContain=" + DEFAULT_DIVORCED);

        // Get all the personList where divorced does not contain UPDATED_DIVORCED
        defaultPersonShouldBeFound("divorced.doesNotContain=" + UPDATED_DIVORCED);
    }

    @Test
    @Transactional
    void getAllPeopleByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name equals to DEFAULT_NAME
        defaultPersonShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the personList where name equals to UPDATED_NAME
        defaultPersonShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByNameIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPersonShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the personList where name equals to UPDATED_NAME
        defaultPersonShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name is not null
        defaultPersonShouldBeFound("name.specified=true");

        // Get all the personList where name is null
        defaultPersonShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByNameContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name contains DEFAULT_NAME
        defaultPersonShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the personList where name contains UPDATED_NAME
        defaultPersonShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByNameNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where name does not contain DEFAULT_NAME
        defaultPersonShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the personList where name does not contain UPDATED_NAME
        defaultPersonShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPeopleByImgIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where img equals to DEFAULT_IMG
        defaultPersonShouldBeFound("img.equals=" + DEFAULT_IMG);

        // Get all the personList where img equals to UPDATED_IMG
        defaultPersonShouldNotBeFound("img.equals=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllPeopleByImgIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where img in DEFAULT_IMG or UPDATED_IMG
        defaultPersonShouldBeFound("img.in=" + DEFAULT_IMG + "," + UPDATED_IMG);

        // Get all the personList where img equals to UPDATED_IMG
        defaultPersonShouldNotBeFound("img.in=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllPeopleByImgIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where img is not null
        defaultPersonShouldBeFound("img.specified=true");

        // Get all the personList where img is null
        defaultPersonShouldNotBeFound("img.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByImgContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where img contains DEFAULT_IMG
        defaultPersonShouldBeFound("img.contains=" + DEFAULT_IMG);

        // Get all the personList where img contains UPDATED_IMG
        defaultPersonShouldNotBeFound("img.contains=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllPeopleByImgNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where img does not contain DEFAULT_IMG
        defaultPersonShouldNotBeFound("img.doesNotContain=" + DEFAULT_IMG);

        // Get all the personList where img does not contain UPDATED_IMG
        defaultPersonShouldBeFound("img.doesNotContain=" + UPDATED_IMG);
    }

    @Test
    @Transactional
    void getAllPeopleByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where gender equals to DEFAULT_GENDER
        defaultPersonShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the personList where gender equals to UPDATED_GENDER
        defaultPersonShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllPeopleByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultPersonShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the personList where gender equals to UPDATED_GENDER
        defaultPersonShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllPeopleByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where gender is not null
        defaultPersonShouldBeFound("gender.specified=true");

        // Get all the personList where gender is null
        defaultPersonShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByBornIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where born equals to DEFAULT_BORN
        defaultPersonShouldBeFound("born.equals=" + DEFAULT_BORN);

        // Get all the personList where born equals to UPDATED_BORN
        defaultPersonShouldNotBeFound("born.equals=" + UPDATED_BORN);
    }

    @Test
    @Transactional
    void getAllPeopleByBornIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where born in DEFAULT_BORN or UPDATED_BORN
        defaultPersonShouldBeFound("born.in=" + DEFAULT_BORN + "," + UPDATED_BORN);

        // Get all the personList where born equals to UPDATED_BORN
        defaultPersonShouldNotBeFound("born.in=" + UPDATED_BORN);
    }

    @Test
    @Transactional
    void getAllPeopleByBornIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where born is not null
        defaultPersonShouldBeFound("born.specified=true");

        // Get all the personList where born is null
        defaultPersonShouldNotBeFound("born.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByDeathIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where death equals to DEFAULT_DEATH
        defaultPersonShouldBeFound("death.equals=" + DEFAULT_DEATH);

        // Get all the personList where death equals to UPDATED_DEATH
        defaultPersonShouldNotBeFound("death.equals=" + UPDATED_DEATH);
    }

    @Test
    @Transactional
    void getAllPeopleByDeathIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where death in DEFAULT_DEATH or UPDATED_DEATH
        defaultPersonShouldBeFound("death.in=" + DEFAULT_DEATH + "," + UPDATED_DEATH);

        // Get all the personList where death equals to UPDATED_DEATH
        defaultPersonShouldNotBeFound("death.in=" + UPDATED_DEATH);
    }

    @Test
    @Transactional
    void getAllPeopleByDeathIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where death is not null
        defaultPersonShouldBeFound("death.specified=true");

        // Get all the personList where death is null
        defaultPersonShouldNotBeFound("death.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where country equals to DEFAULT_COUNTRY
        defaultPersonShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the personList where country equals to UPDATED_COUNTRY
        defaultPersonShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllPeopleByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultPersonShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the personList where country equals to UPDATED_COUNTRY
        defaultPersonShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllPeopleByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where country is not null
        defaultPersonShouldBeFound("country.specified=true");

        // Get all the personList where country is null
        defaultPersonShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByCountryContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where country contains DEFAULT_COUNTRY
        defaultPersonShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the personList where country contains UPDATED_COUNTRY
        defaultPersonShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllPeopleByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where country does not contain DEFAULT_COUNTRY
        defaultPersonShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the personList where country does not contain UPDATED_COUNTRY
        defaultPersonShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllPeopleByNationalityIsEqualToSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nationality equals to DEFAULT_NATIONALITY
        defaultPersonShouldBeFound("nationality.equals=" + DEFAULT_NATIONALITY);

        // Get all the personList where nationality equals to UPDATED_NATIONALITY
        defaultPersonShouldNotBeFound("nationality.equals=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllPeopleByNationalityIsInShouldWork() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nationality in DEFAULT_NATIONALITY or UPDATED_NATIONALITY
        defaultPersonShouldBeFound("nationality.in=" + DEFAULT_NATIONALITY + "," + UPDATED_NATIONALITY);

        // Get all the personList where nationality equals to UPDATED_NATIONALITY
        defaultPersonShouldNotBeFound("nationality.in=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllPeopleByNationalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nationality is not null
        defaultPersonShouldBeFound("nationality.specified=true");

        // Get all the personList where nationality is null
        defaultPersonShouldNotBeFound("nationality.specified=false");
    }

    @Test
    @Transactional
    void getAllPeopleByNationalityContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nationality contains DEFAULT_NATIONALITY
        defaultPersonShouldBeFound("nationality.contains=" + DEFAULT_NATIONALITY);

        // Get all the personList where nationality contains UPDATED_NATIONALITY
        defaultPersonShouldNotBeFound("nationality.contains=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllPeopleByNationalityNotContainsSomething() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        // Get all the personList where nationality does not contain DEFAULT_NATIONALITY
        defaultPersonShouldNotBeFound("nationality.doesNotContain=" + DEFAULT_NATIONALITY);

        // Get all the personList where nationality does not contain UPDATED_NATIONALITY
        defaultPersonShouldBeFound("nationality.doesNotContain=" + UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void getAllPeopleByFatherIsEqualToSomething() throws Exception {
        Person father;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            personRepository.saveAndFlush(person);
            father = PersonResourceIT.createEntity(em);
        } else {
            father = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(father);
        em.flush();
        person.setFather(father);
        personRepository.saveAndFlush(person);
        Long fatherId = father.getId();

        // Get all the personList where father equals to fatherId
        defaultPersonShouldBeFound("fatherId.equals=" + fatherId);

        // Get all the personList where father equals to (fatherId + 1)
        defaultPersonShouldNotBeFound("fatherId.equals=" + (fatherId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByMotherIsEqualToSomething() throws Exception {
        Person mother;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            personRepository.saveAndFlush(person);
            mother = PersonResourceIT.createEntity(em);
        } else {
            mother = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(mother);
        em.flush();
        person.setMother(mother);
        personRepository.saveAndFlush(person);
        Long motherId = mother.getId();

        // Get all the personList where mother equals to motherId
        defaultPersonShouldBeFound("motherId.equals=" + motherId);

        // Get all the personList where mother equals to (motherId + 1)
        defaultPersonShouldNotBeFound("motherId.equals=" + (motherId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleByPersonIsEqualToSomething() throws Exception {
        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            personRepository.saveAndFlush(person);
            person = PersonResourceIT.createEntity(em);
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(person);
        em.flush();
        person.addPerson(person);
        personRepository.saveAndFlush(person);
        Long personId = person.getId();

        // Get all the personList where person equals to personId
        defaultPersonShouldBeFound("personId.equals=" + personId);

        // Get all the personList where person equals to (personId + 1)
        defaultPersonShouldNotBeFound("personId.equals=" + (personId + 1));
    }

    @Test
    @Transactional
    void getAllPeopleBySpouseIsEqualToSomething() throws Exception {
        Person spouse;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            personRepository.saveAndFlush(person);
            spouse = PersonResourceIT.createEntity(em);
        } else {
            spouse = TestUtil.findAll(em, Person.class).get(0);
        }
        em.persist(spouse);
        em.flush();
        person.addSpouse(spouse);
        personRepository.saveAndFlush(person);
        Long spouseId = spouse.getId();

        // Get all the personList where spouse equals to spouseId
        defaultPersonShouldBeFound("spouseId.equals=" + spouseId);

        // Get all the personList where spouse equals to (spouseId + 1)
        defaultPersonShouldNotBeFound("spouseId.equals=" + (spouseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPersonShouldBeFound(String filter) throws Exception {
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(person.getId().intValue())))
            .andExpect(jsonPath("$.[*].divorced").value(hasItem(DEFAULT_DIVORCED)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].img").value(hasItem(DEFAULT_IMG)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].born").value(hasItem(DEFAULT_BORN.toString())))
            .andExpect(jsonPath("$.[*].death").value(hasItem(DEFAULT_DEATH.toString())))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)));

        // Check, that the count call also returns 1
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPersonShouldNotBeFound(String filter) throws Exception {
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPersonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPerson() throws Exception {
        // Get the person
        restPersonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person
        Person updatedPerson = personRepository.findById(person.getId()).get();
        // Disconnect from session so that the updates on updatedPerson are not directly saved in db
        em.detach(updatedPerson);
        updatedPerson
            .divorced(UPDATED_DIVORCED)
            .name(UPDATED_NAME)
            .img(UPDATED_IMG)
            .gender(UPDATED_GENDER)
            .born(UPDATED_BORN)
            .death(UPDATED_DEATH)
            .country(UPDATED_COUNTRY)
            .nationality(UPDATED_NATIONALITY);
        PersonDTO personDTO = personMapper.toDto(updatedPerson);

        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getDivorced()).isEqualTo(UPDATED_DIVORCED);
        assertThat(testPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPerson.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testPerson.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPerson.getBorn()).isEqualTo(UPDATED_BORN);
        assertThat(testPerson.getDeath()).isEqualTo(UPDATED_DEATH);
        assertThat(testPerson.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPerson.getNationality()).isEqualTo(UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void putNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonWithPatch() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person using partial update
        Person partialUpdatedPerson = new Person();
        partialUpdatedPerson.setId(person.getId());

        partialUpdatedPerson.img(UPDATED_IMG).nationality(UPDATED_NATIONALITY);

        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerson))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getDivorced()).isEqualTo(DEFAULT_DIVORCED);
        assertThat(testPerson.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPerson.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testPerson.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPerson.getBorn()).isEqualTo(DEFAULT_BORN);
        assertThat(testPerson.getDeath()).isEqualTo(DEFAULT_DEATH);
        assertThat(testPerson.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testPerson.getNationality()).isEqualTo(UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void fullUpdatePersonWithPatch() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeUpdate = personRepository.findAll().size();

        // Update the person using partial update
        Person partialUpdatedPerson = new Person();
        partialUpdatedPerson.setId(person.getId());

        partialUpdatedPerson
            .divorced(UPDATED_DIVORCED)
            .name(UPDATED_NAME)
            .img(UPDATED_IMG)
            .gender(UPDATED_GENDER)
            .born(UPDATED_BORN)
            .death(UPDATED_DEATH)
            .country(UPDATED_COUNTRY)
            .nationality(UPDATED_NATIONALITY);

        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerson.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPerson))
            )
            .andExpect(status().isOk());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
        Person testPerson = personList.get(personList.size() - 1);
        assertThat(testPerson.getDivorced()).isEqualTo(UPDATED_DIVORCED);
        assertThat(testPerson.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPerson.getImg()).isEqualTo(UPDATED_IMG);
        assertThat(testPerson.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPerson.getBorn()).isEqualTo(UPDATED_BORN);
        assertThat(testPerson.getDeath()).isEqualTo(UPDATED_DEATH);
        assertThat(testPerson.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testPerson.getNationality()).isEqualTo(UPDATED_NATIONALITY);
    }

    @Test
    @Transactional
    void patchNonExistingPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerson() throws Exception {
        int databaseSizeBeforeUpdate = personRepository.findAll().size();
        person.setId(count.incrementAndGet());

        // Create the Person
        PersonDTO personDTO = personMapper.toDto(person);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Person in the database
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerson() throws Exception {
        // Initialize the database
        personRepository.saveAndFlush(person);

        int databaseSizeBeforeDelete = personRepository.findAll().size();

        // Delete the person
        restPersonMockMvc
            .perform(delete(ENTITY_API_URL_ID, person.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Person> personList = personRepository.findAll();
        assertThat(personList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
