package com.pickandgo.controller;

import com.pickandgo.dto.ProduitDTO;
import com.pickandgo.mapper.DTOMapper;
import com.pickandgo.model.Produit;
import com.pickandgo.service.ProduitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {
        try {
            List<Produit> produits = produitService.getAllProduits();
            List<ProduitDTO> produitDTOs = produits.stream()
                    .map(DTOMapper::convertToDTO) // Utiliser la version sans produits similaires
                    .collect(Collectors.toList());

            return ResponseEntity.ok(produitDTOs);
        } catch (Exception e) {
            e.printStackTrace(); // Ajouter pour déboguer
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduitById(@PathVariable Integer id) {
        ProduitDTO produit = produitService.getProduitById(id);
        if (produit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(produit);
    }
    
    @GetMapping("/{id}/similaires")
    public ResponseEntity<List<ProduitDTO>> getProduitsSimilaires(@PathVariable Integer id) {
        try {
            List<Produit> produitsSimilaires = produitService.recommanderProduitsSimilaires(id);
            List<ProduitDTO> produitDTOs = DTOMapper.convertToProduitDTOList(produitsSimilaires);
            return ResponseEntity.ok(produitDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload-json")
    public ResponseEntity<?> uploadJson(@RequestParam("file") MultipartFile file) {
        try {
            Produit produit = produitService.creerProduitDepuisFichier(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(DTOMapper.convertToDTO(produit));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/recommandations/{idU}")
    public ResponseEntity<List<Map<String, Object>>> recommanderProduits(@PathVariable Integer idU) {
        List<Map<String, Object>> recommandations = produitService.recommanderProduitsPourUtilisateur(idU);
        return ResponseEntity.ok(recommandations);
    }



    @PostMapping
    public ResponseEntity<ProduitDTO> createProduit(@RequestBody ProduitDTO produitDTO) {
        try {
            // Conversion simplifiée pour l'exemple - à adapter selon votre modèle complet
            Produit produit = new Produit();
            produit.setNom(produitDTO.getNom());
            produit.setMarque(produitDTO.getMarque());
            produit.setPrixUnitaire(produitDTO.getPrixUnitaire());
            produit.setPrixKg(produitDTO.getPrixKg());
            produit.setPoids(produitDTO.getPoids());
            produit.setConditionnement(produitDTO.getConditionnement());
            produit.setBio(produitDTO.getBio());
            produit.setNutri(produitDTO.getNutri());
            produit.setUrlImage(produitDTO.getUrlImage());
            
            // Cette partie nécessite probablement d'autres services pour récupérer les objets complets
            // Utilisez creerProduit avec NouveauProduitDTO à la place
            
            Produit savedProduit = produitService.getAllProduits().get(0); // Temporaire - à remplacer par la véritable sauvegarde
            
            return new ResponseEntity<>(DTOMapper.convertToDTO(savedProduit), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
