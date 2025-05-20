package com.pickandgo.model;

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
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomP", length = 100)
    private String nomP;

    @Column(name = "prixUnitaireP", precision = 10, scale = 2)
    private BigDecimal prixUnitaireP;

    @Column(name = "prixKgP", precision = 10, scale = 2)
    private BigDecimal prixKgP;

    @Column(name = "poidsP")
    private Integer poidsP;

    @Size(max = 10)
    @Column(name = "nutriP", length = 10)
    private String nutriP;

    @Size(max = 100)
    @Column(name = "conditionnementP", length = 100)
    private String conditionnementP;

    @Column(name = "bioP")
    private Boolean bioP;

    @Size(max = 100)
    @Column(name = "marqueP", length = 100)
    private String marqueP;

    @Size(max = 255)
    @Column(name = "urlImage")
    private String urlImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCate")
    private Categorie idCate;

}