package com.pickandgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ListeDeCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idL", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomL", length = 100)
    private String nomL;

}