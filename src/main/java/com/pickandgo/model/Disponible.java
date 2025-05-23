package com.pickandgo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Disponible {
    @EmbeddedId
    private DisponibleId id;

    @MapsId("idCr")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCr", nullable = false)
    private Creneau idCr;

    @MapsId("idDate")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idDate", nullable = false)
    private Jour idDate;

    @MapsId("idM")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idM", nullable = false)
    private Magasin idM;

    @Column(name = "dispo")
    private Boolean dispo;

}