package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Lister")
public class Lister {

    @EmbeddedId
    private ListerId id;

    @JsonIgnore
    @ManyToOne
    @MapsId("idL")
    @JoinColumn(name = "idL", referencedColumnName = "idL")
    private ListeDeCourse liste;

    @ManyToOne
    @MapsId("idP")
    @JoinColumn(name = "idP", referencedColumnName = "idP")
    private Produit produit;

    @Column(name = "quantiteL")
    private Integer quantite;
}
