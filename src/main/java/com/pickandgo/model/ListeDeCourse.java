package com.pickandgo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idU")
    private Utilisateur utilisateur;
}