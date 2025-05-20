package com.pickandgo.controller;

import com.pickandgo.dto.AjoutProduitPanierDTO;
import com.pickandgo.dto.SupprimerProduitPanierDTO;
import com.pickandgo.model.Panier;
import com.pickandgo.service.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paniers")
public class PanierController {

    private final PanierService panierService;

    @Autowired
    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<Panier> getPanierByUtilisateur(@PathVariable Integer id) {
        Panier panier = panierService.getPanierEnCoursUtilisateur(id);
        return ResponseEntity.ok(panier);
    }

    @PostMapping("/ajouter-produit")
    public ResponseEntity<Panier> ajouterProduit(@RequestBody AjoutProduitPanierDTO dto) {
        Panier panier = panierService.ajouterProduitAuPanier(dto);
        return ResponseEntity.status(HttpStatus.OK).body(panier);
    }

    @PostMapping("/supprimer-produit")
    public ResponseEntity<Panier> supprimerProduit(@RequestBody SupprimerProduitPanierDTO dto) {
        Panier panier = panierService.supprimerProduitDuPanier(dto);
        return ResponseEntity.ok(panier);
    }

    @PutMapping("/{id}/confirmer")
    public ResponseEntity<Panier> confirmerPanier(@PathVariable Integer id) {
        Panier panier = panierService.confirmerPanier(id);
        return ResponseEntity.ok(panier);
    }

    @PutMapping("/{id}/recuperer")
    public ResponseEntity<Panier> marquerCommeRecupere(@PathVariable Integer id) {
        Panier panier = panierService.marquerCommeRecupere(id);
        return ResponseEntity.ok(panier);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerPanier(@PathVariable Integer id) {
        try {
            panierService.supprimerPanier(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}