package com.pickandgo.controller;

import com.pickandgo.model.Produit;
import com.pickandgo.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    private final ProduitService produitService;

    @Autowired
    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProduits() {
        return ResponseEntity.ok(produitService.getAllProduits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduitById(@PathVariable Integer id) {
        Produit produit = produitService.getProduitById(id);
        if (produit != null) {
            return ResponseEntity.ok(produit);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé avec l'ID: " + id);
        }
    }
}