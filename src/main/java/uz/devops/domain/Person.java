package uz.devops.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import uz.devops.domain.enumeration.Gender;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "divorced")
    private String divorced;

    @Column(name = "name")
    private String name;

    @Column(name = "img")
    private String img;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "born")
    private Instant born;

    @Column(name = "death")
    private Instant death;

    @Column(name = "country")
    private String country;

    @Column(name = "nationality")
    private String nationality;

    @ManyToOne
    @JsonIgnoreProperties(value = { "father", "mother", "people", "spouses" }, allowSetters = true)
    private Person father;

    @ManyToOne
    @JsonIgnoreProperties(value = { "father", "mother", "people", "spouses" }, allowSetters = true)
    private Person mother;

    @ManyToMany
    @JoinTable(
        name = "rel_person_spouce",
        joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "spouce_id", referencedColumnName = "id")
    )
    @JsonIgnoreProperties(value = { "father", "mother", "people", "spouses" }, allowSetters = true)
    private Set<Person> people = new HashSet<>();

    @ManyToMany(mappedBy = "people")
    @JsonIgnoreProperties(value = { "father", "mother", "people", "spouses" }, allowSetters = true)
    private Set<Person> spouses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Person id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDivorced() {
        return this.divorced;
    }

    public Person divorced(String divorced) {
        this.setDivorced(divorced);
        return this;
    }

    public void setDivorced(String divorced) {
        this.divorced = divorced;
    }

    public String getName() {
        return this.name;
    }

    public Person name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return this.img;
    }

    public Person img(String img) {
        this.setImg(img);
        return this;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Person gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Instant getBorn() {
        return this.born;
    }

    public Person born(Instant born) {
        this.setBorn(born);
        return this;
    }

    public void setBorn(Instant born) {
        this.born = born;
    }

    public Instant getDeath() {
        return this.death;
    }

    public Person death(Instant death) {
        this.setDeath(death);
        return this;
    }

    public void setDeath(Instant death) {
        this.death = death;
    }

    public String getCountry() {
        return this.country;
    }

    public Person country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationality() {
        return this.nationality;
    }

    public Person nationality(String nationality) {
        this.setNationality(nationality);
        return this;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Person getFather() {
        return this.father;
    }

    public void setFather(Person person) {
        this.father = person;
    }

    public Person father(Person person) {
        this.setFather(person);
        return this;
    }

    public Person getMother() {
        return this.mother;
    }

    public void setMother(Person person) {
        this.mother = person;
    }

    public Person mother(Person person) {
        this.setMother(person);
        return this;
    }

    public Set<Person> getPeople() {
        return this.people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    public Person people(Set<Person> people) {
        this.setPeople(people);
        return this;
    }

    public Person addPerson(Person person) {
        this.people.add(person);
        person.getSpouses().add(this);
        return this;
    }

    public Person removePerson(Person person) {
        this.people.remove(person);
        person.getSpouses().remove(this);
        return this;
    }

    public Set<Person> getSpouses() {
        return this.spouses;
    }

    public void setSpouses(Set<Person> people) {
        if (this.spouses != null) {
            this.spouses.forEach(i -> i.removePerson(this));
        }
        if (people != null) {
            people.forEach(i -> i.addPerson(this));
        }
        this.spouses = people;
    }

    public Person spouses(Set<Person> people) {
        this.setSpouses(people);
        return this;
    }

    public Person addSpouse(Person person) {
        this.spouses.add(person);
        person.getPeople().add(this);
        return this;
    }

    public Person removeSpouse(Person person) {
        this.spouses.remove(person);
        person.getPeople().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Person{" +
            "id=" + getId() +
            ", divorced='" + getDivorced() + "'" +
            ", name='" + getName() + "'" +
            ", img='" + getImg() + "'" +
            ", gender='" + getGender() + "'" +
            ", born='" + getBorn() + "'" +
            ", death='" + getDeath() + "'" +
            ", country='" + getCountry() + "'" +
            ", nationality='" + getNationality() + "'" +
            "}";
    }
}
