package com.pickandgo.model;

public class MotCle {
    private final int idMc;
    private String motMc;

    public MotCle(int idMc, String motMc) {
        this.idMc = idMc;
        this.motMc = motMc;
    }
    public int getIdMc() {
        return idMc;
    }
    public String getMotMc() {
        return motMc;
    }
    public void setMotMc(String motMc) {
        this.motMc = motMc;
    }

    @Override
    public String toString() {
        return "MotCle{" +
                "idMc=" + idMc +
                ", motMc='" + motMc + '\'' +
                '}';
    }
}
