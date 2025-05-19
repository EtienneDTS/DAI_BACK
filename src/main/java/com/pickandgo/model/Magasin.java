package com.pickandgo.model;

import java.util.Objects;

public class Magasin {
    private int idMagasin;
    private String nomMagasin;
    private String adresseMagasin;

    public Magasin(String nomMagasin, String adresseMagasin) {
        this.nomMagasin = nomMagasin;
        this.adresseMagasin = adresseMagasin;
    }

    public int getIdMagasin() {
        return idMagasin;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Magasin magasin = (Magasin) o;
        return idMagasin == magasin.idMagasin && Objects.equals(nomMagasin, magasin.nomMagasin) && Objects.equals(adresseMagasin, magasin.adresseMagasin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMagasin, nomMagasin, adresseMagasin);
    }

    @Override
    public String toString() {
        return "Magasin{" +
                "idMagasin=" + idMagasin +
                ", nomMagasin='" + nomMagasin + '\'' +
                ", adresseMagasin='" + adresseMagasin + '\'' +
                '}';
    }
}
