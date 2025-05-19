package com.pickandgo.model;

import java.util.Date;
import java.util.Objects;

public class Commande {
    private int idCommande;
    private int idClient;
    private double prixTotalCommande;
    private Date dateCommande;
    private StatutC statutCommande;

    public Commande(int idClient) {
        this.idClient = idClient;
        this.prixTotalCommande = prixTotalCommande;
        this.dateCommande = dateCommande;
        this.statutCommande = statutCommande;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }
    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public double getPrixTotalCommande() {
        return prixTotalCommande;
    }

    public void setPrixTotalCommande(double prixTotalCommande) {
        this.prixTotalCommande = prixTotalCommande;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public StatutC getStatutCommande() {
        return statutCommande;
    }

    public void setStatutCommande(StatutC statutCommande) {
        this.statutCommande = statutCommande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commande commande = (Commande) o;
        return idCommande == commande.idCommande && Double.compare(prixTotalCommande, commande.prixTotalCommande) == 0 && Objects.equals(idClient, commande.idClient) && Objects.equals(dateCommande, commande.dateCommande) && statutCommande == commande.statutCommande;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCommande, idClient, prixTotalCommande, dateCommande, statutCommande);
    }

    @Override
    public String toString() {
        return "Commande{" +
                "idCommande=" + idCommande +
                ", idClient='" + idClient + '\'' +
                ", prixTotalCommande=" + prixTotalCommande +
                ", dateCommande=" + dateCommande +
                ", statutCommande=" + statutCommande +
                '}';
    }
}
