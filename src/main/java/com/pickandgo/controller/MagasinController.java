package com.pickandgo.controller;

import com.pickandgo.model.Magasin;
import com.pickandgo.service.MagasinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/magasins")
@CrossOrigin(origins = "*")
public class MagasinController {

    @Autowired
    private MagasinService magasinService;

    @GetMapping
    public ResponseEntity<List<Magasin>> getMagasins() {
        List<Magasin> magasins = magasinService.getTousLesMagasins();
        return ResponseEntity.ok(magasins);
    }

    @GetMapping("/{idmagasin}/panier/{idpanier}/indisponible")
    public ResponseEntity<?> getMagasinsAvecDisponibiliteAlternative(
            @PathVariable("idpanier") Integer idPanier,
            @PathVariable("idmagasin") Integer idMagasin) {

        List<Map<String, Integer>> ids = magasinService.getMagasinsAvecDisponibiliteCompleteFormatee(idPanier, idMagasin);

        if (ids.isEmpty()) {
            return ResponseEntity.ok("Ce panier n'est disponible dans aucun autre magasin.");
        }

        return ResponseEntity.ok(ids);
    }

}
