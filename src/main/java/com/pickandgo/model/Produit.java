package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idP", nullable = false)
    @JsonProperty("idP")
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomP", length = 100)
    @JsonProperty("nomP")
    private String nomP;

    @Column(name = "prixUnitaireP", precision = 10, scale = 2)
    @JsonProperty("prixUnitaireP")
    private BigDecimal prixUnitaireP;

    @Column(name = "prixKgP", precision = 10, scale = 2)
    @JsonProperty("prixKgP")
    private BigDecimal prixKgP;

    @Column(name = "poidsP")
    @JsonProperty("poidsP")
    private Integer poidsP;

    @Size(max = 10)
    @Column(name = "nutriP", length = 10)
    @JsonProperty("nutriP")
    private String nutriP;

    @Size(max = 100)
    @Column(name = "conditionnementP", length = 100)
    @JsonProperty("conditionnementP")
    private String conditionnementP;

    @Column(name = "bioP")
    @JsonProperty("bioP")
    private Boolean bioP;

    @Size(max = 100)
    @Column(name = "marqueP", length = 100)
    @JsonProperty("marqueP")
    private String marqueP;

    @Size(max = 255)
    @Column(name = "urlImage")
    @JsonProperty("urlImage")
    private String urlImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCate")
    private Categorie idCate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idR")
    private Rayon rayon;

    @ManyToMany
    @JoinTable(
            name = "Definir",
            joinColumns = @JoinColumn(name = "idP"),
            inverseJoinColumns = @JoinColumn(name = "idMc")
    )
    private List<MotCle> motsCles = new ArrayList<>();

    @Transient
    @JsonProperty("categorie")
    public String getNomCategorie() {
        return idCate != null ? idCate.getNomCate() : null;
    }

    @Transient
    @JsonProperty("rayon")
    public String getNomRayon() {
        return rayon != null ? rayon.getNomR() : null;
    }

    @Transient
    @JsonProperty("motsCles")
    public List<String> getListeMotsCles() {
        return motsCles.stream()
                .map(MotCle::getMotMc)
                .toList();
    }
}

