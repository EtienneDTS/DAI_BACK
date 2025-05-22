package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Categorie")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCate", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomCate", length = 100)
    private String nomCate;

    //Ajout pour récupérer la catégorie d'un produit
    @OneToMany(mappedBy = "idCate")
    @JsonIgnore
    private List<Produit> produits = new ArrayList<>();

}