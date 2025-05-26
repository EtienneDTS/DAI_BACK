package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pickandgo.config.ApplicationContextProvider;
import com.pickandgo.repository.ProduitRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private String nom;

    @Column(name = "prixUnitaireP", precision = 10, scale = 2)
    @JsonProperty("prixUnitaire")
    private BigDecimal prixUnitaire;

    @Column(name = "prixKgP", precision = 10, scale = 2)
    @JsonProperty("prixKg")
    private BigDecimal prixKg;

    @Column(name = "poidsP")
    @JsonProperty("poids")
    private Integer poids;

    @Size(max = 10)
    @Column(name = "nutriP", length = 10)
    @JsonProperty("nutri")
    private String nutri;

    @Size(max = 100)
    @Column(name = "conditionnementP", length = 100)
    @JsonProperty("conditionnement")
    private String conditionnement;

    @Column(name = "bioP")
    @JsonProperty("bio")
    private Boolean bio;

    @Size(max = 100)
    @Column(name = "marqueP", length = 100)
    @JsonProperty("marque")
    private String marque;

    @Size(max = 255)
    @Column(name = "urlImage")
    @JsonProperty("urlImage")
    private String urlImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCate")
    @JsonIgnore
    private Categorie idCate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idR")
    @JsonIgnore
    private Rayon rayon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPr")
    @JsonIgnore
    private Promotion promotion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Definir",
            joinColumns = @JoinColumn(name = "idP"),
            inverseJoinColumns = @JoinColumn(name = "idMc")
    )
    @JsonIgnore
    private List<MotCle> motsCles = new ArrayList<>();

    @OneToMany(mappedBy = "produit", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Stocker> stockages = new ArrayList<>();

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

    @Transient
    @JsonProperty("promotion")
    public Promotion getPromotion() {
        return promotion;
    }

    // Les méthodes commentées sont supprimées pour éviter la récursion infinie
}
