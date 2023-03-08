package uz.devops.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import uz.devops.domain.enumeration.Gender;

/**
 * Criteria class for the {@link uz.devops.domain.Person} entity. This class is used
 * in {@link uz.devops.web.rest.PersonResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /people?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private StringFilter id;

    private StringFilter name;

    private StringFilter img;

    private GenderFilter gender;

    private InstantFilter born;

    private InstantFilter death;

    private StringFilter country;

    private StringFilter nationality;

    private StringFilter fatherId;

    private StringFilter motherId;

    private StringFilter personId;

    private StringFilter spouseId;

    private StringFilter divorcedPeopleId;

    private StringFilter divorceesId;

    private Boolean distinct;

    public PersonCriteria() {}

    public PersonCriteria(PersonCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.img = other.img == null ? null : other.img.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.born = other.born == null ? null : other.born.copy();
        this.death = other.death == null ? null : other.death.copy();
        this.country = other.country == null ? null : other.country.copy();
        this.nationality = other.nationality == null ? null : other.nationality.copy();
        this.fatherId = other.fatherId == null ? null : other.fatherId.copy();
        this.motherId = other.motherId == null ? null : other.motherId.copy();
        this.personId = other.personId == null ? null : other.personId.copy();
        this.spouseId = other.spouseId == null ? null : other.spouseId.copy();
        this.divorcedPeopleId = other.divorcedPeopleId == null ? null : other.divorcedPeopleId.copy();
        this.divorceesId = other.divorceesId == null ? null : other.divorceesId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PersonCriteria copy() {
        return new PersonCriteria(this);
    }

    public StringFilter getId() {
        return id;
    }

    public StringFilter id() {
        if (id == null) {
            id = new StringFilter();
        }
        return id;
    }

    public void setId(StringFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getImg() {
        return img;
    }

    public StringFilter img() {
        if (img == null) {
            img = new StringFilter();
        }
        return img;
    }

    public void setImg(StringFilter img) {
        this.img = img;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public GenderFilter gender() {
        if (gender == null) {
            gender = new GenderFilter();
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public InstantFilter getBorn() {
        return born;
    }

    public InstantFilter born() {
        if (born == null) {
            born = new InstantFilter();
        }
        return born;
    }

    public void setBorn(InstantFilter born) {
        this.born = born;
    }

    public InstantFilter getDeath() {
        return death;
    }

    public InstantFilter death() {
        if (death == null) {
            death = new InstantFilter();
        }
        return death;
    }

    public void setDeath(InstantFilter death) {
        this.death = death;
    }

    public StringFilter getCountry() {
        return country;
    }

    public StringFilter country() {
        if (country == null) {
            country = new StringFilter();
        }
        return country;
    }

    public void setCountry(StringFilter country) {
        this.country = country;
    }

    public StringFilter getNationality() {
        return nationality;
    }

    public StringFilter nationality() {
        if (nationality == null) {
            nationality = new StringFilter();
        }
        return nationality;
    }

    public void setNationality(StringFilter nationality) {
        this.nationality = nationality;
    }

    public StringFilter getFatherId() {
        return fatherId;
    }

    public StringFilter fatherId() {
        if (fatherId == null) {
            fatherId = new StringFilter();
        }
        return fatherId;
    }

    public void setFatherId(StringFilter fatherId) {
        this.fatherId = fatherId;
    }

    public StringFilter getMotherId() {
        return motherId;
    }

    public StringFilter motherId() {
        if (motherId == null) {
            motherId = new StringFilter();
        }
        return motherId;
    }

    public void setMotherId(StringFilter motherId) {
        this.motherId = motherId;
    }

    public StringFilter getPersonId() {
        return personId;
    }

    public StringFilter personId() {
        if (personId == null) {
            personId = new StringFilter();
        }
        return personId;
    }

    public void setPersonId(StringFilter personId) {
        this.personId = personId;
    }

    public StringFilter getSpouseId() {
        return spouseId;
    }

    public StringFilter spouseId() {
        if (spouseId == null) {
            spouseId = new StringFilter();
        }
        return spouseId;
    }

    public void setSpouseId(StringFilter spouseId) {
        this.spouseId = spouseId;
    }

    public StringFilter getDivorcedPeopleId() {
        return divorcedPeopleId;
    }

    public StringFilter divorcedPeopleId() {
        if (divorcedPeopleId == null) {
            divorcedPeopleId = new StringFilter();
        }
        return divorcedPeopleId;
    }

    public void setDivorcedPeopleId(StringFilter divorcedPeopleId) {
        this.divorcedPeopleId = divorcedPeopleId;
    }

    public StringFilter getDivorceesId() {
        return divorceesId;
    }

    public StringFilter divorceesId() {
        if (divorceesId == null) {
            divorceesId = new StringFilter();
        }
        return divorceesId;
    }

    public void setDivorceesId(StringFilter divorceesId) {
        this.divorceesId = divorceesId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PersonCriteria that = (PersonCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(img, that.img) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(born, that.born) &&
            Objects.equals(death, that.death) &&
            Objects.equals(country, that.country) &&
            Objects.equals(nationality, that.nationality) &&
            Objects.equals(fatherId, that.fatherId) &&
            Objects.equals(motherId, that.motherId) &&
            Objects.equals(personId, that.personId) &&
            Objects.equals(spouseId, that.spouseId) &&
            Objects.equals(divorcedPeopleId, that.divorcedPeopleId) &&
            Objects.equals(divorceesId, that.divorceesId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            img,
            gender,
            born,
            death,
            country,
            nationality,
            fatherId,
            motherId,
            personId,
            spouseId,
            divorcedPeopleId,
            divorceesId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (img != null ? "img=" + img + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (born != null ? "born=" + born + ", " : "") +
            (death != null ? "death=" + death + ", " : "") +
            (country != null ? "country=" + country + ", " : "") +
            (nationality != null ? "nationality=" + nationality + ", " : "") +
            (fatherId != null ? "fatherId=" + fatherId + ", " : "") +
            (motherId != null ? "motherId=" + motherId + ", " : "") +
            (personId != null ? "personId=" + personId + ", " : "") +
            (spouseId != null ? "spouseId=" + spouseId + ", " : "") +
            (divorcedPeopleId != null ? "divorcedPeopleId=" + divorcedPeopleId + ", " : "") +
            (divorceesId != null ? "divorceesId=" + divorceesId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
