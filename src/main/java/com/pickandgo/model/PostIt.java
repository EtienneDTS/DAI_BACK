package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idL", nullable = false)
    private ListeDeCourse liste;

}