package io.github.pascalgrimaud.toto.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link io.github.pascalgrimaud.toto.domain.Alpha} entity. This class is used
 * in {@link io.github.pascalgrimaud.toto.web.rest.AlphaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /alphas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AlphaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstname;

    private StringFilter lastname;

    private LocalDateFilter birthday;

    public AlphaCriteria(){
    }

    public AlphaCriteria(AlphaCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.firstname = other.firstname == null ? null : other.firstname.copy();
        this.lastname = other.lastname == null ? null : other.lastname.copy();
        this.birthday = other.birthday == null ? null : other.birthday.copy();
    }

    @Override
    public AlphaCriteria copy() {
        return new AlphaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstname() {
        return firstname;
    }

    public void setFirstname(StringFilter firstname) {
        this.firstname = firstname;
    }

    public StringFilter getLastname() {
        return lastname;
    }

    public void setLastname(StringFilter lastname) {
        this.lastname = lastname;
    }

    public LocalDateFilter getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateFilter birthday) {
        this.birthday = birthday;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AlphaCriteria that = (AlphaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstname, that.firstname) &&
            Objects.equals(lastname, that.lastname) &&
            Objects.equals(birthday, that.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstname,
        lastname,
        birthday
        );
    }

    @Override
    public String toString() {
        return "AlphaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstname != null ? "firstname=" + firstname + ", " : "") +
                (lastname != null ? "lastname=" + lastname + ", " : "") +
                (birthday != null ? "birthday=" + birthday + ", " : "") +
            "}";
    }

}
