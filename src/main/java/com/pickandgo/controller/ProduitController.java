package com.pickandgo.controller;

import com.pickandgo.dto.NouveauProduitDTO;
import com.pickandgo.model.Produit;
import com.pickandgo.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Produit", description = "API pour la gestion des produits")
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

    @PostMapping
    @Operation(summary = "Créer un nouveau produit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produit créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide (champs manquants ou erronés)"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    public ResponseEntity<Produit> creerProduit(@RequestBody NouveauProduitDTO nouveauProduitDTO) {
        Produit produitCree = produitService.creerProduit(nouveauProduitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(produitCree);
    }

    @PostMapping("/upload-json")
    public ResponseEntity<?> uploadJson(@RequestParam("file") MultipartFile file) {
        try {
            Produit produit = produitService.creerProduitDepuisFichier(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(produit);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}