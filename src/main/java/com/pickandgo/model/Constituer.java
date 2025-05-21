package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

@Entity
@Table(name = "Constituer")
@Getter
@Setter
public class Constituer {

    @EmbeddedId
    private ConstituerPK id;

    @ManyToOne
    @MapsId("panierId")
    @JoinColumn(name = "idPa")
    @JsonBackReference
    private Panier panier;

    @ManyToOne
    @MapsId("produitId")
    @JoinColumn(name = "idP")
    @JsonIdentityReference(alwaysAsId = true)
    private Produit produit;

    @Column(name = "quantiteP")
    private Integer quantite = 0;

    // Constructeurs, equals et hashCode
    public Constituer() {
        this.id = new ConstituerPK();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constituer that = (Constituer) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}