package com.pickandgo.controller;

import com.pickandgo.dto.ApprovisionnementDTO;
import com.pickandgo.model.*;
import com.pickandgo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restocker")
@CrossOrigin(origins = "http://localhost:5173")
public class StockerController {

    @Autowired
    private StockerRepository stockerRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private MagasinRepository magasinRepository;

    @PostMapping("/6/stock/ajouter")
    public ResponseEntity<?> ajouterStockPourBordearouge(@RequestBody ApprovisionnementDTO dto) {
        int idMagasin = 6;

        Produit produit = produitRepository.findById(dto.getIdProduit())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        Magasin magasin = magasinRepository.findById(idMagasin)
                .orElseThrow(() -> new RuntimeException("Magasin non trouvé"));

        StockerId id = new StockerId(dto.getIdProduit(), idMagasin);
        Stocker stocker = stockerRepository.findById(id).orElse(null);

        if (stocker == null) {
            stocker = new Stocker();
            stocker.setId(id);
            stocker.setProduit(produit);
            stocker.setMagasin(magasin);
            stocker.setQuantite(dto.getQuantiteS());
        } else {
            stocker.setQuantite(stocker.getQuantite() + dto.getQuantiteS());
        }

        stockerRepository.save(stocker);
        return ResponseEntity.ok(" Stock mis à jour pour le produit : " + produit.getNom());
    }
}
