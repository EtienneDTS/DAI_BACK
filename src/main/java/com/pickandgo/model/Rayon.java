package com.pickandgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Rayon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idR", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomR", length = 100)
    private String nomR;

}