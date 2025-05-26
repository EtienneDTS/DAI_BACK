package com.pickandgo.dto;

public class ListerDTO {
    private Long idL;
    private ProduitDTO produit;
    private Integer quantiteL;

    // Constructeurs
    public ListerDTO() {
    }

    public ListerDTO(Long idL, ProduitDTO produit, Integer quantiteL) {
        this.idL = idL;
        this.produit = produit;
        this.quantiteL = quantiteL;
    }

    // Getters et Setters
    public Long getIdL() {
        return idL;
    }

    public void setIdL(Long idL) {
        this.idL = idL;
    }

    public ProduitDTO getProduit() {
        return produit;
    }

    public void setProduit(ProduitDTO produit) {
        this.produit = produit;
    }

    public Integer getQuantiteL() {
        return quantiteL;
    }

    public void setQuantiteL(Integer quantiteL) {
        this.quantiteL = quantiteL;
    }
}
