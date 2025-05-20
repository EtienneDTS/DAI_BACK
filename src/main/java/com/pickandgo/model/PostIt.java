package com.pickandgo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PostIt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPI", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "texte")
    private String texte;

}