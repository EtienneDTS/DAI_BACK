package com.pickandgo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class DisponibleId implements java.io.Serializable {
    private static final long serialVersionUID = 6663060700890721196L;
    @NotNull
    @Column(name = "idCr", nullable = false)
    private Integer idCr;

    @NotNull
    @Column(name = "idDate", nullable = false)
    private Integer idDate;

    @NotNull
    @Column(name = "idM", nullable = false)
    private Integer idM;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DisponibleId entity = (DisponibleId) o;
        return Objects.equals(this.idM, entity.idM) &&
                Objects.equals(this.idDate, entity.idDate) &&
                Objects.equals(this.idCr, entity.idCr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idM, idDate, idCr);
    }

}