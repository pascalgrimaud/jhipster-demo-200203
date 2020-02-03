package io.github.pascalgrimaud.toto.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link io.github.pascalgrimaud.toto.domain.Alpha} entity.
 */
public class AlphaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 5)
    private String firstname;

    private String lastname;

    @NotNull
    private LocalDate birthday;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
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

        AlphaDTO alphaDTO = (AlphaDTO) o;
        if (alphaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), alphaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlphaDTO{" +
            "id=" + getId() +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", birthday='" + getBirthday() + "'" +
            "}";
    }
}
