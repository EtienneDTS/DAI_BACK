package com.pickandgo.model;

public class ListeDeCourse {
    private final int idL;
    private String nomL;

    public ListeDeCourse(int idL, String nomL) {
        this.idL = idL;
        this.nomL = nomL;
    }

    public int getIdL() {
        return idL;
    }
    public String getNomL() {
        return nomL;
    }
    public void setNomL(String nomL) {
         this.nomL=nomL;
    }

    @Override
    public String toString() {
        return "ListeDeCourse{" +
                "idL=" + idL +
                ", nomL='" + nomL + '\'' +
                '}';
    }
}
