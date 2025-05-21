package com.pickandgo.service;

import com.pickandgo.dto.NouveauProduitDTO;
import com.pickandgo.model.Categorie;
import com.pickandgo.model.Produit;
import com.pickandgo.repository.CategorieRepository;
import com.pickandgo.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;

    @Autowired
    public ProduitService(ProduitRepository produitRepository, CategorieRepository categorieRepository) {
        this.produitRepository = produitRepository;
        this.categorieRepository = categorieRepository;
    }

    @Transactional
    public Produit creerProduit(NouveauProduitDTO dto) {
        // Vérifier que la catégorie existe
        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        // Créer le nouveau produit
        Produit nouveauProduit = new Produit();
        nouveauProduit.setNomP(dto.getNomP());
        nouveauProduit.setPrixUnitaireP(dto.getPrixUnitaireP());
        nouveauProduit.setPrixKgP(dto.getPrixKgP());
        nouveauProduit.setPoidsP(dto.getPoidsP());
        nouveauProduit.setNutriP(dto.getNutriP());
        nouveauProduit.setConditionnementP(dto.getConditionnementP());
        nouveauProduit.setBioP(dto.getBioP());
        nouveauProduit.setMarqueP(dto.getMarqueP());
        nouveauProduit.setUrlImage(dto.getUrlImage());
        nouveauProduit.setIdCate(categorie);

        // Sauvegarder et retourner le produit
        return produitRepository.save(nouveauProduit);
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Integer id) {
        return produitRepository.findById(id).orElse(null);
    }
}