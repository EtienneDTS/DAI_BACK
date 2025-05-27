package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Commander {
    @EmbeddedId
    private CommanderId id;

    @MapsId("idPa")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idPa", nullable = false)
    @JsonBackReference("panier-commande")
    private Panier idPa;

    @MapsId("idM")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idM", nullable = false)
    private Magasin idM;

    @Column(name = "dateC")
    private LocalDate dateC;

    @Size(max = 255)
    @Column(name = "creneauChoisi")
    private String creneauChoisi;

}