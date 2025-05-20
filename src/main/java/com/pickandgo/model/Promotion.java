package com.pickandgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPr", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomPr", length = 100)
    private String nomPr;

    @Size(max = 255)
    @Column(name = "urlImagePromo")
    private String urlImagePromo;

    @Size(max = 255)
    @Column(name = "typePr")
    private String typePr;

}