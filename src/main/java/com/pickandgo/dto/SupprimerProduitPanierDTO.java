package com.pickandgo.dto;

import lombok.Data;

@Data
public class SupprimerProduitPanierDTO {
    private Integer idPanier;
    private Integer idProduit;
    private Integer quantite; // Quantité à retirer, null pour supprimer complètement
}