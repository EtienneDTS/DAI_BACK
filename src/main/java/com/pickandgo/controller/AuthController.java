package com.pickandgo.controller;

import com.pickandgo.model.Panier;
import com.pickandgo.model.Utilisateur;
import com.pickandgo.service.PanierService;
import com.pickandgo.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

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

        // Trouve le panier actif
        Panier panierActif = panierService.trouverPanierActifParUtilisateur(utilisateurConnecte.getId());

        // Convertit Utilisateur en Map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> utilisateurMap = mapper.convertValue(utilisateurConnecte, Map.class);

        // Injecte le champ panierActifId
        utilisateurMap.put("panierActifId", panierActif != null ? panierActif.getIdPanier() : null);

        // Réponse JSON complète
        Map<String, Object> reponse = new HashMap<>();
        reponse.put("utilisateur", utilisateurMap);

        // Gère les cas sessionId ou panierId comme avant...
        if (sessionId != null && !sessionId.isEmpty()) {
            try {
                panierService.fusionnerPanierAnonymeVersUtilisateur(sessionId, utilisateurConnecte.getId());
                reponse.put("panierFusionne", true);
            } catch (Exception e) {
                reponse.put("panierFusionne", false);
                reponse.put("erreur", e.getMessage());
            }
        }

        if (panierId != null) {
            try {
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