package com.pickandgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MotCle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMc", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "motMc", length = 100)
    private String motMc;

}