package com.pickandgo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AjouterProduitPanierDTO {
    private Integer idPanier;
    private Integer idProduit;
    private Integer quantite;
}
