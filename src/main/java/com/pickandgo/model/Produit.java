package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "Produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idP", nullable = false)
    @JsonProperty("id")
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomP", length = 100)
    @JsonProperty("nom")
    private String nomP;

    @Column(name = "prixUnitaireP", precision = 10, scale = 2)
    @JsonProperty("prixUnitaire")
    private BigDecimal prixUnitaireP;

    @Column(name = "prixKgP", precision = 10, scale = 2)
    @JsonProperty("prixKg")
    private BigDecimal prixKgP;

    @Column(name = "poidsP")
    @JsonProperty("poid")
    private Integer poidsP;

    @Size(max = 10)
    @Column(name = "nutriP", length = 10)
    @JsonProperty("nutriScore")
    private String nutriP;

    @Size(max = 100)
    @Column(name = "conditionnementP", length = 100)
    @JsonProperty("conditionnement")
    private String conditionnementP;

    @Column(name = "bioP")
    @JsonProperty("bio")
    private Boolean bioP;

    @Size(max = 100)
    @Column(name = "marqueP", length = 100)
    @JsonProperty("marque")
    private String marqueP;

    @Size(max = 255)
    @Column(name = "urlImage")
    @JsonProperty("urlImage")
    private String urlImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCate")
    private Categorie idCate;

}