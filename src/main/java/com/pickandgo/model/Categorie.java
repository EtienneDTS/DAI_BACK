package com.pickandgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCate", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomCate", length = 100)
    private String nomCate;

}