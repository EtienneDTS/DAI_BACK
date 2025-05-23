package com.pickandgo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Jour {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "dateJour")
    private LocalDate dateJour;

}