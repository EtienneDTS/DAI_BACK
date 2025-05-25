package com.pickandgo.model;

import com.pickandgo.model.DefinirId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Definir {
    @EmbeddedId
    private DefinirId id;

    @MapsId("idMc")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idMc", nullable = false)
    private MotCle idMc;

    @MapsId("idP")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idP", nullable = false)
    private Produit idP;

}