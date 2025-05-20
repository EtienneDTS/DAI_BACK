package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idU", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomU", length = 100)
    private String nomU;

    @Size(max = 100)
    @Column(name = "prenomU", length = 100)
    private String prenomU;

    @Size(max = 50)
    @Column(name = "role", length = 50)
    private String role;

    @Size(max = 100)
    @Column(name = "emailU", length = 100)
    private String emailU;

    @Size(max = 255)
    @Column(name = "motDePasse")
    @JsonIgnore
    private String motDePasse;

    @Lob
    @Column(name = "adresseU")
    private String adresseU;

}