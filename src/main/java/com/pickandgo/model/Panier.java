package com.pickandgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPa", nullable = false)
    private Integer id;

    @Column(name = "prixtotalPa", precision = 10, scale = 2)
    private BigDecimal prixtotalPa;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

}