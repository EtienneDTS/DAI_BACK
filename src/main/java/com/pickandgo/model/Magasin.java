package com.pickandgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Magasin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idM", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomM", length = 100)
    private String nomM;

    @Lob
    @Column(name = "adresseM")
    private String adresseM;

}