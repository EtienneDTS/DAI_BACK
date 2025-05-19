package com.pickandgo.model;

import java.util.ArrayList;

public class Panier {
    private int idPanier;
    private ArrayList<Produit> produitsPanier;
    private double prixTotalPanier;

    public Panier() {
        this.idPanier = idPanier;
        this.produitsPanier = new ArrayList<>();
    }
}
