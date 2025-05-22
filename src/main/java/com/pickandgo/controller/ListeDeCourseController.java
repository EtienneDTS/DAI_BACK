package com.pickandgo.controller;


import com.pickandgo.model.ListeDeCourse;
import com.pickandgo.service.ListeDeCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/listes")
@CrossOrigin(origins = "http://localhost:5173")
public class ListeDeCourseController {

    private final ListeDeCourseService listeDeCourseService;

    @Autowired
    public ListeDeCourseController(ListeDeCourseService listeDeCourseService) {
        this.listeDeCourseService = listeDeCourseService;
    }

    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<List<ListeDeCourse>> getListesByUtilisateur(@PathVariable Integer id) {
        return ResponseEntity.ok(listeDeCourseService.getListesByUtilisateur(id));
    }

    @PostMapping
    public ResponseEntity<Object> createListe(@RequestBody Map<String, Object> payload) {
        String nom = (String) payload.get("noml");
        Integer idUtilisateur = (Integer) payload.get("idUtilisateur");

        ListeDeCourse createdListe = listeDeCourseService.createListe(nom, idUtilisateur);

        // Construction manuelle de la réponse JSON propre
        Map<String, Object> response = new HashMap<>();
        response.put("id", createdListe.getId());
        response.put("noml", createdListe.getNoml());
        response.put("produits", createdListe.getProduits());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Ajout des produits à la liste
    @PostMapping("/listes/{id}/produits")
    public ResponseEntity<String> ajouterProduitAListe(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {
        Integer idProduit = body.get("idProduit");
        Integer quantite = body.get("quantite");

        listeDeCourseService.ajouterProduitDansListe(id, idProduit, quantite);

        return ResponseEntity.ok("Produit ajouté");
    }

    //Modifier la quantité d'un produit de la liste
   // public void mettreAJourQuantite(Integer idListe, Integer idProduit, int nouvelleQuantite) {
     //   Optional<Lister> listerOpt = listerRepository.findById(new ListerId(idListe, idProduit));

       // if (listerOpt.isPresent()) {
         //   if (nouvelleQuantite <= 0) {
           //     listerRepository.delete(listerOpt.get());
            //} else {
              //  Lister lister = listerOpt.get();
               // lister.setQuantite(nouvelleQuantite);
                //listerRepository.save(lister);
            //}
        //} else {
          //  throw new RuntimeException("Produit non trouvé dans la liste");
        //}
   // }
}