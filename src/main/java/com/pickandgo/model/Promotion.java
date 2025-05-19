package com.pickandgo.model;

public class Promotion {
    private final int idPR;
    private String nomP;

    public Promotion(int idPR, String nomP) {
        this.idPR = idPR;
        this.nomP = nomP;
    }

    public int getIdPR() {
        return idPR;
    }
    public String getNomP() {
        return nomP;
    }
    public void setNomP(String nomP) {
        this.nomP = nomP;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "idPR=" + idPR +
                ", nomP='" + nomP + '\'' +
                '}';
    }
}
