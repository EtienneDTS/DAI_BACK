package com.pickandgo.controller;

import com.pickandgo.dto.ProduitDTO;
import com.pickandgo.mapper.DTOMapper;
import com.pickandgo.model.Produit;
import com.pickandgo.service.ProduitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "*")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    @GetMapping
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {
        try {
            List<Produit> produits = produitService.getAllProduits();
            List<ProduitDTO> produitDTOs = DTOMapper.convertToProduitDTOList(produits);
            return ResponseEntity.ok(produitDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> getProduitById(@PathVariable Integer id) {
        try {
            Produit produit = produitService.getProduitById(id);
            if (produit != null) {
                ProduitDTO produitDTO = DTOMapper.convertToDTO(produit);
                return ResponseEntity.ok(produitDTO);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @PutMapping("/{id}")
    public ResponseEntity<ProduitDTO> updateProduit(@PathVariable Integer id, @RequestBody ProduitDTO produitDTO) {
        try {
            Produit existingProduit = produitService.getProduitById(id);
            if (existingProduit != null) {
                // Mise à jour des attributs simples
                existingProduit.setNom(produitDTO.getNom());
                existingProduit.setMarque(produitDTO.getMarque());
                existingProduit.setPrixUnitaire(produitDTO.getPrixUnitaire());
                existingProduit.setPrixKg(produitDTO.getPrixKg());
                existingProduit.setPoids(produitDTO.getPoids());
                existingProduit.setConditionnement(produitDTO.getConditionnement());
                existingProduit.setBio(produitDTO.getBio());
                existingProduit.setNutri(produitDTO.getNutri());
                existingProduit.setUrlImage(produitDTO.getUrlImage());
                
                // Pour terminer la mise à jour avec les associations, vous auriez besoin d'autres services
                
                // Cette partie est temporaire et devrait être remplacée par la véritable mise à jour
                return ResponseEntity.ok(DTOMapper.convertToDTO(existingProduit));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Integer id) {
        try {
            // Vérifier si le produit existe avant de le supprimer
            Produit existingProduit = produitService.getProduitById(id);
            if (existingProduit != null) {
                // Implémentez la méthode deleteProduit dans votre service
                // produitService.deleteProduit(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
