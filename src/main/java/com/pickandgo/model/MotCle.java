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
public class MotCle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMc", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "motMc", length = 100)
    private String motMc;

    @ManyToMany(mappedBy = "motsCles")
    @JsonIgnore
    private List<Produit> produits = new ArrayList<>();

}