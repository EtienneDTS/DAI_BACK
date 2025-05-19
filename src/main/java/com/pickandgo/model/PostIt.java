package com.pickandgo.model;

public class PostIt {
    private final int idPt;
    private String texte;

    public PostIt(int idPt, String texte) {
        this.idPt = idPt;
        this.texte = texte;
    }
    public int getIdPt() {
        return idPt;
    }
    public String getTexte() {
        return texte;
    }
    public void setTexte(String texte) {
        this.texte = texte;
    }

    @Override
    public String toString() {
        return "PostIt{" +
                "idPt=" + idPt +
                ", texte='" + texte + '\'' +
                '}';
    }
}
