package com.pickandgo.model;

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
    @JoinColumn(name = "idP") // nom exact dans ta table
    private Produit produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("magasinId")
    @JoinColumn(name = "idM") // nom exact dans ta table
    private Magasin magasin;

    @Column(name = "quantiteS") // nom exact dans ta table
    private Integer quantite;
}
