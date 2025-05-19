package com.pickandgo.model;

import java.util.Date;

public class Prevision {
    private final int idP;
    private Date datePre;

    public Prevision(int idP, Date datePre) {
        this.idP = idP;
        this.datePre = datePre;
    }

    public int getIdP() {
        return idP;
    }
    public Date getDatePre() {
        return datePre;
    }
    public void setDatePre(Date datePre) {
        this.datePre = datePre;
    }

    @Override
    public String toString() {
        return "Prevision{" +
                "idP=" + idP +
                ", datePre=" + datePre +
                '}';
    }
}
