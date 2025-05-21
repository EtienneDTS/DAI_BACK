package com.pickandgo.controller;

import com.pickandgo.dto.AjoutProduitPanierDTO;
import com.pickandgo.dto.SupprimerProduitEntierDTO;
import com.pickandgo.dto.SupprimerProduitPanierDTO;
import com.pickandgo.model.Panier;
import com.pickandgo.service.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/paniers")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Panier", description = "API pour la gestion du panier utilisateur")
public class PanierController {

    private final PanierService panierService;

    @Autowired
    public PanierController(PanierService panierService) {
        this.panierService = panierService;
    }

    @GetMapping("/utilisateur/{id}")
    @Operation(summary = "Récupérer le panier d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Panier trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<Panier> getPanierByUtilisateur(@PathVariable Integer id) {
        Panier panier = panierService.getPanierUtilisateur(id);
        return ResponseEntity.ok(panier);
    }

    @PostMapping("/ajouter-produit")
    @Operation(summary = "Ajouter un produit au panier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produit ajouté avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou produit non trouvé")
    })
    public ResponseEntity<Panier> ajouterProduit(@RequestBody AjoutProduitPanierDTO dto) {
        Panier panier = panierService.ajouterProduitAuPanier(dto);
        return ResponseEntity.status(HttpStatus.OK).body(panier);
    }

    @PostMapping("/supprimer-produit")
    @Operation(summary = "Supprimer un produit du panier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produit supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé dans le panier")
    })
    public ResponseEntity<Panier> supprimerProduit(@RequestBody SupprimerProduitPanierDTO dto) {
        Panier panier = panierService.supprimerProduitDuPanier(dto);
        return ResponseEntity.ok(panier);
    }

    @PostMapping("/supprimer-produit-entier")
    @Operation(summary = "Supprimer complètement un produit du panier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produit supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé dans le panier")
    })
    public ResponseEntity<Panier> supprimerProduitEntier(@RequestBody SupprimerProduitEntierDTO dto) {
        Panier panier = panierService.supprimerProduitEntier(dto);
        return ResponseEntity.ok(panier);
    }

    @PutMapping("/{id}/confirmer")
    @Operation(summary = "Confirmer un panier pour la préparation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Panier confirmé avec succès"),
            @ApiResponse(responseCode = "400", description = "Le panier ne peut pas être confirmé"),
            @ApiResponse(responseCode = "404", description = "Panier non trouvé")
    })
    public ResponseEntity<Panier> confirmerPanier(@PathVariable Integer id) {
        Panier panier = panierService.confirmerPanier(id);
        return ResponseEntity.ok(panier);
    }

    @PutMapping("/{id}/recuperer")
    @Operation(summary = "Marquer un panier comme récupéré")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Panier marqué comme récupéré avec succès"),
            @ApiResponse(responseCode = "400", description = "Le panier ne peut pas être marqué comme récupéré"),
            @ApiResponse(responseCode = "404", description = "Panier non trouvé")
    })
    public ResponseEntity<Panier> marquerCommeRecupere(@PathVariable Integer id) {
        Panier panier = panierService.marquerCommeRecupere(id);
        return ResponseEntity.ok(panier);
    }

    @PutMapping("/{id}/vider")
    @Operation(summary = "Vider le panier d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Panier vidé avec succès"),
            @ApiResponse(responseCode = "404", description = "Panier non trouvé")
    })
    public ResponseEntity<Panier> viderPanier(@PathVariable Integer id) {
        Panier panier = panierService.viderPanier(id);
        return ResponseEntity.ok(panier);
    }
}