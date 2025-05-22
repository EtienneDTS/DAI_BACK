package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ListeDeCourse")
public class ListeDeCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idL", nullable = false)
    private Integer id;

    @Column(name = "nomL", length = 100)
    private String noml;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idU")
    private Utilisateur utilisateur;

    @JsonIgnore
    @OneToMany(mappedBy = "liste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lister> liaisonsProduits = new ArrayList<>();

    @Transient
    public List<Produit> getProduits() {
        return liaisonsProduits.stream()
                .map(Lister::getProduit)
                .toList(); // ou .collect(Collectors.toList()) si tu es en Java 8
    }


}