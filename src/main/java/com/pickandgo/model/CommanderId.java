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
public class CommanderId implements java.io.Serializable {
    private static final long serialVersionUID = 8963747009118219326L;
    @NotNull
    @Column(name = "idPa", nullable = false)
    private Integer idPa;

    @NotNull
    @Column(name = "idM", nullable = false)
    private Integer idM;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CommanderId entity = (CommanderId) o;
        return Objects.equals(this.idM, entity.idM) &&
                Objects.equals(this.idPa, entity.idPa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idM, idPa);
    }

}