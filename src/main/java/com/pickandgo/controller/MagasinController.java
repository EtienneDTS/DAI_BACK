package com.pickandgo.controller;

import com.pickandgo.model.Magasin;
import com.pickandgo.service.MagasinService;
import com.pickandgo.service.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/magasins")
@CrossOrigin(origins = "http://localhost:5173")
public class MagasinController {

    @Autowired
    private MagasinService magasinService;

    @Autowired
    private PanierService panierService;

    @GetMapping
    public ResponseEntity<List<Magasin>> getMagasins() {
        List<Magasin> magasins = magasinService.getTousLesMagasins();
        return ResponseEntity.ok(magasins);
    }

    //TESTER LA DISPO DANS UN AUTRE MAGASIN
    @GetMapping("/{idmagasin}/panier/{idpanier}/indisponible")
    public ResponseEntity<?> getMagasinsAvecDisponibiliteAlternative(
            @PathVariable("idpanier") Integer idPanier,
            @PathVariable("idmagasin") Integer idMagasin) {

        List<Integer> ids = magasinService.getMagasinsAvecDisponibiliteComplete(idPanier, idMagasin);

        if (ids.isEmpty()) {
            // Retourne un message simple avec HTTP 404 ou 200 selon choix
            return ResponseEntity.ok("Ce panier n'est disponible dans aucun autre magasin.");
        }

        // Sinon, retourne la liste des magasins
        return ResponseEntity.ok(ids);
    }
}
