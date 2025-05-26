package com.pickandgo.service;

import com.pickandgo.dto.NouveauProduitDTO;
import com.pickandgo.model.Categorie;
import com.pickandgo.model.Produit;
import com.pickandgo.model.Promotion;
import com.pickandgo.model.Rayon;
import com.pickandgo.repository.CategorieRepository;
import com.pickandgo.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import com.pickandgo.repository.RayonRepository;
import com.pickandgo.repository.PromotionRepository;


import java.util.List;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;
    private final RayonRepository rayonRepository;
    private final PromotionRepository promotionRepository;

    @Autowired
    public ProduitService(
            ProduitRepository produitRepository,
            CategorieRepository categorieRepository,
            RayonRepository rayonRepository,
            PromotionRepository promotionRepository
    ) {
        this.produitRepository = produitRepository;
        this.categorieRepository = categorieRepository;
        this.rayonRepository = rayonRepository;
        this.promotionRepository = promotionRepository;
    }

    @Transactional
    public Produit creerProduit(NouveauProduitDTO dto) {

        // 🔎 Récupérer le rayon
        Rayon rayon = rayonRepository.findById(dto.getIdR())
                .orElseThrow(() -> new RuntimeException("Rayon non trouvé avec l'ID: " + dto.getIdR()));

        // 🔎 Récupérer la catégorie
        Categorie categorie = categorieRepository.findById(dto.getIdCate())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + dto.getIdCate()));


        // 🔎 Récupérer la promotion (facultative)
        Promotion promotion = null;
        if (dto.getIdPr() != null) {
            promotion = promotionRepository.findById(dto.getIdPr())
                    .orElseThrow(() -> new RuntimeException("Promotion non trouvée avec l'ID: " + dto.getIdPr()));
        }

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
        nouveauProduit.setRayon(rayon);
        nouveauProduit.setIdCate(categorie);
        nouveauProduit.setPromotion(promotion);

        // Sauvegarder et retourner le produit
        return produitRepository.save(nouveauProduit);
    }

    @Transactional
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @Transactional
    public Produit getProduitById(Integer id) {
        return produitRepository.findById(id).orElse(null);
    }
    
    @Transactional
    public Produit creerProduitDepuisFichier(MultipartFile file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NouveauProduitDTO dto = mapper.readValue(file.getInputStream(), NouveauProduitDTO.class);
        return creerProduit(dto);
    }
}