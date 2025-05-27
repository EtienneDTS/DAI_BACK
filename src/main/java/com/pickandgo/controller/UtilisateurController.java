package com.pickandgo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickandgo.model.Panier;
import com.pickandgo.model.Utilisateur;
import com.pickandgo.service.PanierService;
import com.pickandgo.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private PanierService panierService;


    @GetMapping("/ages")
    public ResponseEntity<Map<String, Long>> getStatsAge() {
        return ResponseEntity.ok(utilisateurService.getRepartitionParTrancheAge());
    }

    @PutMapping("/{idUtilisateur}/magasin/{idMagasin}")
    public ResponseEntity<?> changerMagasinUtilisateur(
            @PathVariable Integer idUtilisateur,
            @PathVariable Integer idMagasin) {

        // Met à jour le magasin
        Utilisateur utilisateurMisAJour = utilisateurService.changerMagasinUtilisateur(idUtilisateur, idMagasin);

        // Récupère le panier actif
        Panier panierActif = panierService.trouverPanierActifParUtilisateur(utilisateurMisAJour.getId());

        // Convertit Utilisateur en Map JSON
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> utilisateurMap = mapper.convertValue(utilisateurMisAJour, Map.class);

        utilisateurMap.put("panierActifId", panierActif != null ? panierActif.getIdPanier() : null);

        Map<String, Object> reponse = new HashMap<>();
        reponse.put("utilisateur", utilisateurMap);

        return ResponseEntity.ok(reponse);
    }

}




