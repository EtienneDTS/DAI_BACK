package com.pickandgo.controller;

import com.pickandgo.dto.AjouterProduitDTO;
import com.pickandgo.dto.ModifierQuantiteProduitDTO;
import com.pickandgo.dto.SupprimerProduitEntierDTO;
import com.pickandgo.dto.RetraitSelectionDTO;
import com.pickandgo.model.Panier;
import com.pickandgo.service.PanierService;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/panier")
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
    @Operation(summary = "Ajouter un produit au panier d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produit ajouté avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou produit non trouvé")
    })
    public ResponseEntity<Panier> ajouterProduitAuPanier(@RequestBody AjouterProduitDTO dto) {
        Panier panier = panierService.ajouterProduitAuPanierUtilisateur(
                dto.getIdUtilisateur(),
                dto.getIdProduit(),
                dto.getQuantite());
        return ResponseEntity.ok(panier);
    }

    @PostMapping("/modifier-quantite")
    @Operation(summary = "Modifier la quantité d'un produit dans le panier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantité modifiée avec succès"),
            @ApiResponse(responseCode = "404", description = "Panier ou produit non trouvé")
    })
    public ResponseEntity<Panier> modifierQuantiteProduit(@RequestBody ModifierQuantiteProduitDTO dto) {
        Panier panier = panierService.modifierQuantiteProduit(dto);
        return ResponseEntity.status(HttpStatus.OK).body(panier);
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
    @PutMapping("/{id}/passer-commande")
    @Operation(summary = "Passer une commande à partir du panier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande passée avec succès"),
            @ApiResponse(responseCode = "400", description = "Le panier ne peut pas être commandé"),
            @ApiResponse(responseCode = "401", description = "Authentification requise"),
            @ApiResponse(responseCode = "404", description = "Panier non trouvé")
    })
    public ResponseEntity<?> passerCommande(
            @PathVariable Integer id,
            @RequestParam(required = false) String sessionId,
            Authentication authentication) {
        try {
            // Si utilisateur connecté
            if (authentication != null && authentication.isAuthenticated()) {
                Panier panier = panierService.passerCommande(id, authentication);
                return ResponseEntity.ok(panier);
            }
            // Si panier anonyme
            else if (sessionId != null && !sessionId.isEmpty()) {
                // Renvoyer une réponse 401 avec l'ID du panier pour permettre la redirection
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Authentification requise");
                response.put("panierId", id);
                response.put("sessionId", sessionId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            } else {
                throw new RuntimeException("Vous devez vous connecter pour passer commande");
            }
        } catch (RuntimeException e) {
            if (e.getMessage().contains("connecter pour passer commande")) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Authentification requise");
                response.put("panierId", id);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            throw e;
        }
    }

    @PostMapping("/{id}/choisir-retrait")
    @Operation(summary = "Choisir un magasin, une date et un créneau pour le retrait")
    public ResponseEntity<Map<String, Object>> choisirRetrait(
            @PathVariable Integer id,
            @RequestBody RetraitSelectionDTO selection) {
        Panier panier = panierService.choisirRetraitEtReserverCreneau(id, selection);

        // Créer une réponse qui inclut le panier et les informations de retrait
        Map<String, Object> response = new HashMap<>();
        response.put("panier", panier);
        response.put("magasinId", selection.getMagasinId());
        response.put("jourId", selection.getJourId());
        response.put("creneauId", selection.getCreneauId());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/demarrer-preparation")
    @Operation(summary = "Démarrer la préparation d'une commande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préparation démarrée avec succès"),
            @ApiResponse(responseCode = "400", description = "La commande ne peut pas être préparée"),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée")
    })
    public ResponseEntity<Panier> demarrerPreparation(@PathVariable Integer id) {
        Panier panier = panierService.demarrerPreparation(id);
        return ResponseEntity.ok(panier);
    }

    @PutMapping("/{id}/terminer-preparation")
    @Operation(summary = "Terminer la préparation d'une commande")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préparation terminée avec succès"),
            @ApiResponse(responseCode = "400", description = "La commande ne peut pas être marquée comme prête"),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée")
    })
    public ResponseEntity<Panier> terminerPreparation(@PathVariable Integer id) {
        Panier panier = panierService.terminerPreparation(id);
        return ResponseEntity.ok(panier);
    }

    @PutMapping("/{id}/recuperer")
    @Operation(summary = "Marquer une commande comme récupérée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commande marquée comme récupérée avec succès"),
            @ApiResponse(responseCode = "400", description = "La commande ne peut pas être marquée comme récupérée"),
            @ApiResponse(responseCode = "404", description = "Commande non trouvée")
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

    @PostMapping("/modifier-quantite-anonyme")
    @Operation(summary = "Modifier la quantité d'un produit dans un panier anonyme")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantité modifiée avec succès"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé")
    })
    public ResponseEntity<Panier> modifierQuantiteProduitAnonyme(
            @RequestBody ModifierQuantiteProduitDTO dto,
            @RequestParam String sessionId) {
        Panier panier = panierService.modifierQuantiteProduitAnonyme(dto, sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(panier);
    }

    @PostMapping("/fusionner/{panierId}")
    @Operation(summary = "Fusionner un panier anonyme avec le panier de l'utilisateur connecté")
    public ResponseEntity<Void> fusionnerPaniers(
            @PathVariable Integer panierId,
            @RequestParam String sessionId) {
        panierService.fusionnerPanierAnonymeVersUtilisateur(sessionId, panierId);
        return ResponseEntity.ok().build();
    }


    // MODIFS SO POUR DISPO
    @GetMapping("/utilisateur/{userId}/magasin/{magasinId}")
    @Operation(summary = "Récupérer le panier d'un utilisateur avec les disponibilités en stock par magasin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Panier avec disponibilités trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur ou panier non trouvé")
    })
    public ResponseEntity<Panier> getPanierAvecDisponibilite(
            @PathVariable Integer userId,
            @PathVariable Integer magasinId) {
        Panier panier = panierService.getPanierUtilisateurAvecDisponibilite(userId, magasinId);
        return ResponseEntity.ok(panier);
    }

    //RECUPERER TOUTES LES COMMANDES D'UN MAGASIN (HORS PANIER)
    @GetMapping("/magasin/{magasinId}/commandes")
    @Operation(summary = "Récupérer toutes les commandes d'un magasin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commandes trouvées"),
            @ApiResponse(responseCode = "404", description = "Magasin non trouvé")
    })
    public ResponseEntity<?> getCommandesParMagasin(@PathVariable Integer magasinId) {
        try {
            return ResponseEntity.ok(panierService.getCommandesParMagasin(magasinId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la récupération des commandes : " + e.getMessage());
        }
    }







}