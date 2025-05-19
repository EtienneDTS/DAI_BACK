package com.pickandgo.model;

public class Rayon {
    private final int idR;
    private String nomR;

    public Rayon(int idR, String nomR) {
        this.idR = idR;
        this.nomR = nomR;
    }

    public int getIdR() {
        return idR;
    }
    public String getNomR() {
        return nomR;
    }
    public void setNomR(String nomR) {
        this.nomR = nomR;
    }

    @Override
    public String toString() {
        return "Rayon{" +
                "idR=" + idR +
                ", nomR='" + nomR + '\'' +
                '}';
    }
}
