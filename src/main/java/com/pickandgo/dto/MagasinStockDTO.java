package com.pickandgo.dto;

public class MagasinStockDTO {
    private Integer idMagasin;
    private String nomMagasin;
    private String adresseMagasin;
    private Integer quantiteStock;

    public MagasinStockDTO() {}

    public MagasinStockDTO(Integer idMagasin, String nomMagasin, String adresseMagasin, Integer quantiteStock) {
        this.idMagasin = idMagasin;
        this.nomMagasin = nomMagasin;
        this.adresseMagasin = adresseMagasin;
        this.quantiteStock = quantiteStock;
    }

    // Getters et setters
    public Integer getIdMagasin() {
        return idMagasin;
    }

    public void setIdMagasin(Integer idMagasin) {
        this.idMagasin = idMagasin;
    }

    public String getNomMagasin() {
        return nomMagasin;
    }

    public void setNomMagasin(String nomMagasin) {
        this.nomMagasin = nomMagasin;
    }

    public String getAdresseMagasin() {
        return adresseMagasin;
    }

    public void setAdresseMagasin(String adresseMagasin) {
        this.adresseMagasin = adresseMagasin;
    }

    public Integer getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(Integer quantiteStock) {
        this.quantiteStock = quantiteStock;
    }
}