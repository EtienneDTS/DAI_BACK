package com.pickandgo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class StockerId implements Serializable {

    @Column(name = "idP")
    private Integer produitId;

    @Column(name = "idM")
    private Integer magasinId;
}
