package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "Stocker")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stocker {

    @EmbeddedId
    private StockerId id;

    @ManyToOne
    @MapsId("produitId")
    @JoinColumn(name = "idP")
    @JsonBackReference
    private Produit produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("magasinId")
    @JoinColumn(name = "idM")
    private Magasin magasin;

    @Column(name = "quantiteS")
    private Integer quantite;
}
