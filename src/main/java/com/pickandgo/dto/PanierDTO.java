package com.pickandgo.dto;

import java.util.List;

public class PanierDTO {
    private Integer idPa;
    private List<ConstituerDTO> produitsPanier;
    
    // Constructeurs
    public PanierDTO() {
    }
    
    public PanierDTO(Integer idPa, List<ConstituerDTO> produitsPanier) {
        this.idPa = idPa;
        this.produitsPanier = produitsPanier;
    }
    
    // Getters et Setters
    public Integer getIdPa() {
        return idPa;
    }
    
    public void setIdPa(Integer idPa) {
        this.idPa = idPa;
    }
    
    public List<ConstituerDTO> getProduitsPanier() {
        return produitsPanier;
    }
    
    public void setProduitsPanier(List<ConstituerDTO> produitsPanier) {
        this.produitsPanier = produitsPanier;
    }
}
