package com.pickandgo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifierQuantiteProduitDTO {
    private Integer idPanier;
    private Integer idProduit;
    private Integer nouvelleQuantite; // Peut augmenter ou diminuer la quantité existante
}