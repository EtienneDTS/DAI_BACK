package com.pickandgo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {
    @Id
    @GeneratedValue
    @Column(name = "idP")
    private int idProduit;
    @Column(name = "nomP")
    private String nomProduit;
    @Column(name = "")
    private boolean estDisponible;
    @Column(name = "")
    private String urlImgProduit;
    @Column(name = "prixUnitaireP")
    private double prixUnitProduit;
    @Column(name = "prixKgP")
    private double prixKgProduit;
    @Column(name = "uniteP")
    private int uniteProduit;
    @Column(name = "poidsP")
    private double poidsProduit;
    @Column(name = "nutriP")
    private Nutriscore nutriscoreProduit;
    @Column(name = "bioP")
    private boolean estBio;
    @Column(name = "marqueP")
    private String marqueProduit;
    @Column(name = "idCate")
    private int idCategorieProduit;

    public Produit(String nomProduit, double prixUnitProduit, double prixKgProduit,
                   int nbUnitesProduit, double poidsProduit, Nutriscore nutriscoreProduit,
                   boolean estBio, String marqueProduit, int idCategorieProduit) {
        this.nomProduit = nomProduit;
        this.prixUnitProduit = prixUnitProduit;
        this.prixKgProduit = prixKgProduit;
        this.uniteProduit = nbUnitesProduit;
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
        return uniteProduit;
    }

    public void setNbUnitesProduit(int nbUnitesProduit) {
        this.uniteProduit = nbUnitesProduit;
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
        return idProduit == produit.idProduit && Double.compare(prixUnitProduit, produit.prixUnitProduit) == 0 && Double.compare(prixKgProduit, produit.prixKgProduit) == 0 && uniteProduit == produit.uniteProduit && Double.compare(poidsProduit, produit.poidsProduit) == 0 && estBio == produit.estBio && idCategorieProduit == produit.idCategorieProduit && Objects.equals(nomProduit, produit.nomProduit) && nutriscoreProduit == produit.nutriscoreProduit && Objects.equals(marqueProduit, produit.marqueProduit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduit, nomProduit, prixUnitProduit, prixKgProduit, uniteProduit, poidsProduit, nutriscoreProduit, estBio, marqueProduit, idCategorieProduit);
    }

    @Override
    public String toString() {
        return "Produit{" +
                "idProduit=" + idProduit +
                ", nomProduit='" + nomProduit + '\'' +
                ", prixUnitProduit=" + prixUnitProduit +
                ", prixKgProduit=" + prixKgProduit +
                ", nbUnitesProduit=" + uniteProduit +
                ", poidsProduit=" + poidsProduit +
                ", nutriscoreProduit=" + nutriscoreProduit +
                ", estBio=" + estBio +
                ", marqueProduit='" + marqueProduit + '\'' +
                ", idCategorieProduit=" + idCategorieProduit +
                '}';
    }

}
