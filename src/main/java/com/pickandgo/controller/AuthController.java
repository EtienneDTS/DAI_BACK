package com.pickandgo.controller;

import com.pickandgo.model.Panier;
import com.pickandgo.model.Utilisateur;
import com.pickandgo.service.PanierService;
import com.pickandgo.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private PanierService panierService;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(
            @RequestBody Utilisateur credentials,
            @RequestParam(required = false) String sessionId,
            @RequestParam(required = false) Integer panierId) {

        String email = credentials.getEmailU();
        String motDePasse = credentials.getMotDePasse();

        Utilisateur utilisateurConnecte = utilisateurService.verifierConnexion(email, motDePasse);

        if (utilisateurConnecte == null) {
            return ResponseEntity.badRequest().body("Identifiants incorrects");
        }

        Map<String, Object> reponse = new HashMap<>();
        reponse.put("utilisateur", utilisateurConnecte);

        // Cas 1: Fusion de panier anonyme avec sessionId (navigation normale)
        if (sessionId != null && !sessionId.isEmpty()) {
            try {
                panierService.fusionnerPanierAnonymeVersUtilisateur(sessionId, utilisateurConnecte.getId());
                reponse.put("panierFusionne", true);
            } catch (Exception e) {
                reponse.put("panierFusionne", false);
                reponse.put("erreur", e.getMessage());
            }
        }

        // Cas 2: Connexion pendant le processus de commande (avec un panierId)
        if (panierId != null) {
            try {
                // Au lieu de finaliser la commande, on transfère simplement les produits
                // et on retourne le panier pour que l'utilisateur puisse choisir un magasin et un créneau
                Panier panierUtilisateur = panierService.finaliserCommandeApresConnexion(panierId, utilisateurConnecte.getId());

                reponse.put("etapeCommandeEnCours", true);
                reponse.put("panierId", panierUtilisateur.getIdPanier());
                reponse.put("etapeSuivante", "choixRetrait");
                reponse.put("message", "Veuillez sélectionner un magasin et un créneau de retrait pour finaliser votre commande");
            } catch (Exception e) {
                reponse.put("etapeCommandeEnCours", false);
                reponse.put("erreurCommande", e.getMessage());
            }
        }

        return ResponseEntity.ok(reponse);
    }
}