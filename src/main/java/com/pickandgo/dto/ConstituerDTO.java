package com.pickandgo.dto;

public class ConstituerDTO {
    private Integer idPa;
    private ProduitDTO produit;
    private Integer quantiteP;

    // Constructeurs
    public ConstituerDTO() {
    }

    public ConstituerDTO(Integer idPa, ProduitDTO produit, Integer quantiteP) {
        this.idPa = idPa;
        this.produit = produit;
        this.quantiteP = quantiteP;
    }

    // Getters et Setters
    public Integer getIdPa() {
        return idPa;
    }

    public void setIdPa(Integer idPa) {
        this.idPa = idPa;
    }

    public ProduitDTO getProduit() {
        return produit;
    }

    public void setProduit(ProduitDTO produit) {
        this.produit = produit;
    }

    public Integer getQuantite() {
        return quantiteP;
    }

    public void setQuantiteP(Integer quantiteP) {
        this.quantiteP = quantiteP;
    }
}
