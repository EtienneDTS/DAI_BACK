package com.pickandgo.model;

import java.util.Objects;

public class Produit {
    private int idProduit;
    private String nomProduit;
    private boolean estDisponible;
    private String urlImgProduit;
    private double prixUnitProduit;
    private double prixKgProduit;
    private int nbUnitesProduit;
    private double poidsProduit;
    private Nutriscore nutriscoreProduit;
    private boolean estBio;
    private String marqueProduit;
    private int idCategorieProduit;

    public Produit(String nomProduit, double prixUnitProduit, double prixKgProduit,
                   int nbUnitesProduit, double poidsProduit, Nutriscore nutriscoreProduit,
                   boolean estBio, String marqueProduit, int idCategorieProduit) {
        this.nomProduit = nomProduit;
        this.prixUnitProduit = prixUnitProduit;
        this.prixKgProduit = prixKgProduit;
        this.nbUnitesProduit = nbUnitesProduit;
        this.poidsProduit = poidsProduit;
        this.nutriscoreProduit = nutriscoreProduit;
        this.estBio = estBio;
        this.marqueProduit = marqueProduit;
        this.idCategorieProduit = idCategorieProduit;
        this.estDisponible=estDisponible;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setEstDisponible(int idProduit) {
        this.estDisponible = estDisponible;
    }

    public Boolean getEstDisponible() {
        return estDisponible;
    }

    public double getPrixUnitProduit() {
        return prixUnitProduit;
    }

    public void setPrixUnitProduit(double prixUnitProduit) {
        this.prixUnitProduit = prixUnitProduit;
    }

    public double getPrixKgProduit() {
        return prixKgProduit;
    }

    public void setPrixKgProduit(double prixKgProduit) {
        this.prixKgProduit = prixKgProduit;
    }

    public int getNbUnitesProduit() {
        return nbUnitesProduit;
    }

    public void setNbUnitesProduit(int nbUnitesProduit) {
        this.nbUnitesProduit = nbUnitesProduit;
    }

    public double getPoidsProduit() {
        return poidsProduit;
    }

    public void setPoidsProduit(double poidsProduit) {
        this.poidsProduit = poidsProduit;
    }

    public Nutriscore getNutriscoreProduit() {
        return nutriscoreProduit;
    }

    public boolean isEstBio() {
        return estBio;
    }

    public void setEstBio(boolean estBio) {
        this.estBio = estBio;
    }

    public String getMarqueProduit() {
        return marqueProduit;
    }

    public void setMarqueProduit(String marqueProduit) {
        this.marqueProduit = marqueProduit;
    }

    public int getIdCategorieProduit() {
        return idCategorieProduit;
    }

    public void setIdCategorieProduit(int idCategorieProduit) {
        this.idCategorieProduit = idCategorieProduit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produit produit = (Produit) o;
        return idProduit == produit.idProduit && Double.compare(prixUnitProduit, produit.prixUnitProduit) == 0 && Double.compare(prixKgProduit, produit.prixKgProduit) == 0 && nbUnitesProduit == produit.nbUnitesProduit && Double.compare(poidsProduit, produit.poidsProduit) == 0 && estBio == produit.estBio && idCategorieProduit == produit.idCategorieProduit && Objects.equals(nomProduit, produit.nomProduit) && nutriscoreProduit == produit.nutriscoreProduit && Objects.equals(marqueProduit, produit.marqueProduit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduit, nomProduit, prixUnitProduit, prixKgProduit, nbUnitesProduit, poidsProduit, nutriscoreProduit, estBio, marqueProduit, idCategorieProduit);
    }

    @Override
    public String toString() {
        return "Produit{" +
                "idProduit=" + idProduit +
                ", nomProduit='" + nomProduit + '\'' +
                ", prixUnitProduit=" + prixUnitProduit +
                ", prixKgProduit=" + prixKgProduit +
                ", nbUnitesProduit=" + nbUnitesProduit +
                ", poidsProduit=" + poidsProduit +
                ", nutriscoreProduit=" + nutriscoreProduit +
                ", estBio=" + estBio +
                ", marqueProduit='" + marqueProduit + '\'' +
                ", idCategorieProduit=" + idCategorieProduit +
                '}';
    }
}
