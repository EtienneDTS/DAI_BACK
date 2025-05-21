package com.pickandgo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class ConstituerPK implements Serializable {

    @Column(name = "idPa")
    private Integer panierId;

    @Column(name = "idP")
    private Integer produitId;

    // Constructeur par défaut requis par JPA
    public ConstituerPK() {}

    public ConstituerPK(Integer panierId, Integer produitId) {
        this.panierId = panierId;
        this.produitId = produitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConstituerPK that = (ConstituerPK) o;
        return Objects.equals(panierId, that.panierId) &&
                Objects.equals(produitId, that.produitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(panierId, produitId);
    }
}