package com.pickandgo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AjouterProduitDTO {
    private Integer idUtilisateur;
    private Integer idProduit;
    private Integer quantite;
}