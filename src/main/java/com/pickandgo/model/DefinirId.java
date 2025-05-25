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
public class DefinirId implements java.io.Serializable {
    private static final long serialVersionUID = 5389952707354325694L;
    @NotNull
    @Column(name = "idMc", nullable = false)
    private Integer idMc;

    @NotNull
    @Column(name = "idP", nullable = false)
    private Integer idP;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DefinirId entity = (DefinirId) o;
        return Objects.equals(this.idMc, entity.idMc) &&
                Objects.equals(this.idP, entity.idP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMc, idP);
    }

}