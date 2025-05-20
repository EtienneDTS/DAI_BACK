package com.pickandgo.dto;

import lombok.Data;

@Data
public class AjoutProduitPanierDTO {
    private Integer idUtilisateur;
    private Integer idProduit;
    private Integer quantite;
}