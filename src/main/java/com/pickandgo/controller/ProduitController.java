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
            List<ProduitDTO> produits = produitService.getAllProduits();
            return ResponseEntity.ok(produits);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProduitDTO> getProduitById(@PathVariable Integer id) {
        try {
            ProduitDTO produit = produitService.getProduitById(id);
            return ResponseEntity.ok(produit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recommandations/{id}")
    public ResponseEntity<List<ProduitDTO>> getProduitsSimilaires(@PathVariable Integer id) {
        try {
            List<Produit> produitsSimilaires = produitService.recommanderProduitsSimilaires(id);
            List<ProduitDTO> produitDTOs = produitsSimilaires.stream()
                    .map(produit -> DTOMapper.convertToDTO(produit, new ArrayList<>()))
                    .collect(Collectors.toList());
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

            Produit savedProduit = produitService.creerProduit(convertToDTONouveauProduit(produitDTO));

            // Conversion en utilisant la méthode correcte avec liste de produits similaires vide
            return new ResponseEntity<>(DTOMapper.convertToDTO(savedProduit, new ArrayList<>()), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Méthode utilitaire pour convertir ProduitDTO en NouveauProduitDTO
    private com.pickandgo.dto.NouveauProduitDTO convertToDTONouveauProduit(ProduitDTO produitDTO) {
        com.pickandgo.dto.NouveauProduitDTO nouveauDTO = new com.pickandgo.dto.NouveauProduitDTO();
        nouveauDTO.setNomP(produitDTO.getNom());
        nouveauDTO.setMarqueP(produitDTO.getMarque());
        nouveauDTO.setPrixUnitaireP(produitDTO.getPrixUnitaire());
        nouveauDTO.setPrixKgP(produitDTO.getPrixKg());
        nouveauDTO.setPoidsP(produitDTO.getPoids());
        nouveauDTO.setConditionnementP(produitDTO.getConditionnement());
        nouveauDTO.setBioP(produitDTO.getBio());
        nouveauDTO.setNutriP(produitDTO.getNutri());
        nouveauDTO.setUrlImage(produitDTO.getUrlImage());
        nouveauDTO.setIdCate(produitDTO.getIdCate());
        nouveauDTO.setIdR(produitDTO.getIdR());
        nouveauDTO.setIdPr(produitDTO.getIdPr());
        return nouveauDTO;
    }


}
