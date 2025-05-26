package com.pickandgo.controller;

import com.pickandgo.model.ListeDeCourse;
import com.pickandgo.model.Lister;
import com.pickandgo.model.Panier;
import com.pickandgo.model.PostIt;
import com.pickandgo.service.ListeDeCourseService;
import com.pickandgo.service.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/listes")
@CrossOrigin(origins = "http://localhost:5173")
public class ListeDeCourseController {

    private final ListeDeCourseService listeDeCourseService;
    private final PanierService panierService;

    @Autowired
    public ListeDeCourseController(ListeDeCourseService listeDeCourseService, PanierService panierService) {
        this.listeDeCourseService = listeDeCourseService;
        this.panierService = panierService;
    }

    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<List<Map<String, Object>>> getListesByUtilisateur(@PathVariable Integer id) {
        List<ListeDeCourse> listes = listeDeCourseService.getListesByUtilisateur(id);
        List<Map<String, Object>> result = new ArrayList<>();

        for (ListeDeCourse liste : listes) {
            Map<String, Object> listeMap = new HashMap<>();
            listeMap.put("id", liste.getId());
            listeMap.put("noml", liste.getNoml());

            List<Map<String, Object>> produitsAvecQuantite = new ArrayList<>();
            for (Lister lister : liste.getLiaisonsProduits()) {
                Map<String, Object> produitMap = new HashMap<>();
                produitMap.put("produit", lister.getProduit());
                produitMap.put("quantite", lister.getQuantite());
                produitsAvecQuantite.add(produitMap);
            }

            listeMap.put("produits", produitsAvecQuantite);
            result.add(listeMap);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> createListe(@RequestBody Map<String, Object> payload) {
        String nom = (String) payload.get("noml");
        Integer idUtilisateur = (Integer) payload.get("idUtilisateur");

        ListeDeCourse createdListe = listeDeCourseService.createListe(nom, idUtilisateur);

        List<Map<String, Object>> produitsAvecQuantite = new ArrayList<>();
        for (Lister lister : createdListe.getLiaisonsProduits()) {
            Map<String, Object> produitEtQuantite = new HashMap<>();
            produitEtQuantite.put("produit", lister.getProduit());
            produitEtQuantite.put("quantite", lister.getQuantite());
            produitsAvecQuantite.add(produitEtQuantite);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", createdListe.getId());
        response.put("noml", createdListe.getNoml());
        response.put("produits", produitsAvecQuantite);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{idListe}/produits/{idProduit}")
    public ResponseEntity<String> ajouterProduitAListe(
            @PathVariable Integer idListe,
            @PathVariable Integer idProduit,
            @RequestParam(defaultValue = "1") Integer quantite) {

        listeDeCourseService.ajouterProduitDansListe(idListe, idProduit, quantite);

        return ResponseEntity.ok("Produit ajouté");
    }

    @PutMapping("/{idListe}/produits/{idProduit}")
    public ResponseEntity<String> modifierQuantiteProduitDansListe(
            @PathVariable Integer idListe,
            @PathVariable Integer idProduit,
            @RequestParam Integer quantite) {

        if (quantite < 0) {
            return ResponseEntity.badRequest().body("La quantité ne peut pas être négative.");
        }

        boolean updated = listeDeCourseService.mettreAJourQuantite(idListe, idProduit, quantite);

        if (updated) {
            return ResponseEntity.ok("Quantité mise à jour.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé dans la liste.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerListe(@PathVariable Integer id) {
        boolean deleted = listeDeCourseService.supprimerListeParId(id);

        if (deleted) {
            return ResponseEntity.ok("Liste supprimée avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Liste non trouvée.");
        }
    }

    // Afficher une seule liste avec ses produits et ses post-its
    @GetMapping("/{idListe}")
    public ResponseEntity<Map<String, Object>> getListeParId(@PathVariable Integer idListe) {
        Optional<ListeDeCourse> listeOpt = listeDeCourseService.getListeParId(idListe);

        if (listeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        ListeDeCourse liste = listeOpt.get();
        Map<String, Object> listeMap = new HashMap<>();
        listeMap.put("id", liste.getId());
        listeMap.put("noml", liste.getNoml());

        // Produits avec quantités
        List<Map<String, Object>> produitsAvecQuantite = new ArrayList<>();
        for (Lister lister : liste.getLiaisonsProduits()) {
            Map<String, Object> produitMap = new HashMap<>();
            produitMap.put("produit", lister.getProduit());
            produitMap.put("quantite", lister.getQuantite());
            produitsAvecQuantite.add(produitMap);
        }
        listeMap.put("produits", produitsAvecQuantite);

        // Post-its
        List<PostIt> postIts = liste.getPostIts();
        listeMap.put("postIts", postIts);

        return ResponseEntity.ok(listeMap);
    }



    // GESTION DES POSTS IT //
    @PostMapping("/{idListe}/postits")
    public ResponseEntity<PostIt> ajouterPostItVide(@PathVariable Integer idListe) {
        PostIt postItCree = listeDeCourseService.ajouterPostItVide(idListe);
        return ResponseEntity.status(HttpStatus.CREATED).body(postItCree);
    }

    @PutMapping("/postits/{idPostIt}")
    public ResponseEntity<PostIt> modifierPostIt(@PathVariable Integer idPostIt, @RequestBody Map<String, String> payload) {
        String nouveauTexte = payload.get("texte");
        PostIt postItModifie = listeDeCourseService.modifierPostIt(idPostIt, nouveauTexte);
        return ResponseEntity.ok(postItModifie);
    }

    @DeleteMapping("/postits/{idPostIt}")
    public ResponseEntity<Void> supprimerPostIt(@PathVariable Integer idPostIt) {
        listeDeCourseService.supprimerPostIt(idPostIt);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{idListe}/produits/{idProduit}")
    public ResponseEntity<String> supprimerProduitDeListe(
            @PathVariable Integer idListe,
            @PathVariable Integer idProduit) {

        boolean success = listeDeCourseService.supprimerProduitDeListe(idListe, idProduit);

        if (success) {
            return ResponseEntity.ok("Produit supprimé de la liste.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produit non trouvé dans la liste.");
        }
    }



    // CONVERSION D'UNE LISTE EN PANIER
    @PostMapping("/{idListe}/deverser-dans-panier/{idUtilisateur}")
    public ResponseEntity<Panier> deverserProduitsDansPanier(
            @PathVariable Integer idListe,
            @PathVariable Integer idUtilisateur) {

        Optional<ListeDeCourse> optionalListe = listeDeCourseService.getListeParId(idListe);
        if (optionalListe.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        ListeDeCourse liste = optionalListe.get();

        Panier panier = panierService.ajouterProduitsDepuisListeDansPanier(idUtilisateur, liste.getLiaisonsProduits());

        return ResponseEntity.ok(panier);
    }



    //METHODE POUR SUGGERER PRODUITS POST-ITS/HABITUDE

    //if post-it a des mots -clés, on va chercher les produits qui contiennent ces mots-clés


}



