package uz.devops.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import uz.devops.domain.enumeration.Gender;

/**
 * A DTO for the {@link uz.devops.domain.Person} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonDTO implements Serializable {

    private String id;

    private String name;

    private String img;

    private Gender gender;

    private Instant born;

    private Instant death;

    private String country;

    private String nationality;

    private PersonDTO father;

    private PersonDTO mother;

    private Set<PersonDTO> people = new HashSet<>();

    private Set<PersonDTO> divorcedPeople = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Instant getBorn() {
        return born;
    }

    public void setBorn(Instant born) {
        this.born = born;
    }

    public Instant getDeath() {
        return death;
    }

    public void setDeath(Instant death) {
        this.death = death;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public PersonDTO getFather() {
        return father;
    }

    public void setFather(PersonDTO father) {
        this.father = father;
    }

    public PersonDTO getMother() {
        return mother;
    }

    public void setMother(PersonDTO mother) {
        this.mother = mother;
    }

    public Set<PersonDTO> getPeople() {
        return people;
    }

    public void setPeople(Set<PersonDTO> people) {
        this.people = people;
    }

    public Set<PersonDTO> getDivorcedPeople() {
        return divorcedPeople;
    }

    public void setDivorcedPeople(Set<PersonDTO> divorcedPeople) {
        this.divorcedPeople = divorcedPeople;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonDTO)) {
            return false;
        }

        PersonDTO personDTO = (PersonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, personDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", img='" + getImg() + "'" +
            ", gender='" + getGender() + "'" +
            ", born='" + getBorn() + "'" +
            ", death='" + getDeath() + "'" +
            ", country='" + getCountry() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", father=" + getFather() +
            ", mother=" + getMother() +
            ", people=" + getPeople() +
            ", divorcedPeople=" + getDivorcedPeople() +
            "}";
    }
}
