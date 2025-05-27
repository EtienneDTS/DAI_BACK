package com.pickandgo.dto;

import software.amazon.awssdk.services.rds.endpoints.internal.Value;

import java.math.BigDecimal;
import java.util.List;

public class ProduitDTO {
    private Integer id;
    private String nom;
    private String marque;
    private BigDecimal prixUnitaire;
    private BigDecimal prixKg;
    private Integer poids;
    private String conditionnement;
    private Boolean bio;
    private String nutri;
    private String urlImage;
    private Integer idCate;
    private Integer idR;
    private Integer idPr;
    private String nomCategorie;
    private String nomRayon;
    private List<ProduitDTO> produitsSimilaires;
    private PromotionDTO promotion;
    private List<MagasinStockDTO> disponibilites;
    private List<String> motsCles;

    // Constructeurs
    public ProduitDTO(String nomCategorie, String nomRayon) {
        this.nomCategorie = nomCategorie;
        this.nomRayon = nomRayon;
    }

    public ProduitDTO(Integer id, String nomP, String marqueP, BigDecimal prixUnitaireP, BigDecimal prixKgP,
                      Integer poidsP, String conditionnementP, Boolean bioP, String nutriP,
                      String urlImage, Integer idCate, Integer idR, Integer idPr, String nomCategorie, String nomRayon) {
        this.id = id;
        this.nom = nomP;
        this.marque = marqueP;
        this.prixUnitaire = prixUnitaireP;
        this.prixKg = prixKgP;
        this.poids = poidsP;
        this.conditionnement = conditionnementP;
        this.bio = bioP;
        this.nutri = nutriP;
        this.urlImage = urlImage;
        this.idCate = idCate;
        this.idR = idR;
        this.idPr = idPr;
        this.nomCategorie = nomCategorie;
        this.nomRayon = nomRayon;
    }

    public ProduitDTO(Integer id, String nom, String marque, BigDecimal prixUnitaire, BigDecimal prixKg, Integer poids, String conditionnement, Boolean bio, String nutri, String urlImage, Integer integer, Integer integer1, Integer integer2) {
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer idP) {
        this.id = idP;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nomP) {
        this.nom = nomP;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marqueP) {
        this.marque = marqueP;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaireP) {
        this.prixUnitaire = prixUnitaireP;
    }

    public BigDecimal getPrixKg() {
        return prixKg;
    }

    public void setPrixKg(BigDecimal prixKgP) {
        this.prixKg = prixKgP;
    }

    public Integer getPoids() {
        return poids;
    }

    public void setPoids(Integer poidsP) {
        this.poids = poidsP;
    }

    public String getConditionnement() {
        return conditionnement;
    }

    public void setConditionnement(String conditionnementP) {
        this.conditionnement = conditionnementP;
    }

    public Boolean getBio() {
        return bio;
    }

    public void setBio(Boolean bioP) {
        this.bio = bioP;
    }

    public String getNutri() {
        return nutri;
    }

    public void setNutri(String nutriP) {
        this.nutri = nutriP;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public Integer getIdCate() {
        return idCate;
    }

    public void setIdCate(Integer idCate) {
        this.idCate = idCate;
    }

    public Integer getIdR() {
        return idR;
    }

    public void setIdR(Integer idR) {
        this.idR = idR;
    }

    public Integer getIdPr() {
        return idPr;
    }

    public void setIdPr(Integer idPr) {
        this.idPr = idPr;
    }

    public List<ProduitDTO> getProduitsSimilaires() {
        return produitsSimilaires;
    }

    public void setProduitsSimilaires(List<ProduitDTO> produitsSimilaires) {
        this.produitsSimilaires = produitsSimilaires;
    }

    public PromotionDTO getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionDTO promotion) {
        this.promotion = promotion;
    }

    public List<MagasinStockDTO> getDisponibilites() {
        return disponibilites;
    }

    public void setDisponibilites(List<MagasinStockDTO> disponibilites) {
        this.disponibilites = disponibilites;
    }

    public List<String> getMotsCles() {
        return motsCles;
    }

    public void setMotsCles(List<String> motsCles) {
        this.motsCles = motsCles;
    }
    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }
    public void setNomRayon(String nomRayon) {
        this.nomRayon = nomRayon;
    }
    public String getNomRayon() {
        return nomRayon;
    }
}
