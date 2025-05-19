package com.pickandgo.model;

public class Categorie {
    private final int idCate;
    private String nomCate;

    public Categorie(int idCate, String nomCate) {
        this.idCate = idCate;
        this.nomCate = nomCate;
    }

    public int getIdCate() {
        return idCate;
    }
    public String getNomCate() {
        return nomCate;
    }
    public void setNomCate(String nomCate) {
        this.nomCate = nomCate;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "idCate=" + idCate +
                ", nomCate='" + nomCate + '\'' +
                '}';
    }
}
