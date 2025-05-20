package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
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

    @Embeddable
    @Getter
    @Setter
    public static class ConstituerPK implements Serializable {

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