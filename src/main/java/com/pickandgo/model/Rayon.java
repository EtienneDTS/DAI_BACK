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
public class Rayon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idR", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomR", length = 100)
    private String nomR;

    // Ajout pour récupérer le rayon d'un produit
    @OneToMany(mappedBy = "rayon")
    @JsonIgnore
    private List<Produit> produits = new ArrayList<>();
}