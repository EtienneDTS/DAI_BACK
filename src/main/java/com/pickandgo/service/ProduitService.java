package com.pickandgo.service;

import com.pickandgo.dto.NouveauProduitDTO;
import com.pickandgo.dto.ProduitDTO;
import com.pickandgo.mapper.DTOMapper;
import com.pickandgo.model.Categorie;
import com.pickandgo.model.MotCle;
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


import java.util.Collections;
import java.util.HashMap;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<Produit> recommanderProduitsSimilaires(Integer idProduit) {
        int nombreRecommandations = 3;

        Produit produitReference = produitRepository.findById(idProduit)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id: " + idProduit));

        List<MotCle> motsClesProduit = produitReference.getMotsCles();
        if (motsClesProduit.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> idMotsCles = motsClesProduit.stream()
                .map(MotCle::getId)
                .collect(Collectors.toList());

        // Récupérer les produits avec leur nombre de mots clés correspondants
        Map<Integer, Long> produitIdToMatchCount = produitRepository.findProductIdsWithMatchCount(idMotsCles, idProduit);

        if (produitIdToMatchCount.isEmpty()) {
            return Collections.emptyList();
        }

        // Trier par nombre de mots-clés correspondants (décroissant)
        List<Integer> produitSimilaireIds = produitIdToMatchCount.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(nombreRecommandations)
                .collect(Collectors.toList());

        // Récupérer les produits triés avec leurs associations
        List<Produit> produitsSimilaires = produitRepository.findAllByIdWithAssociations(produitSimilaireIds);

        // Préserver l'ordre de tri basé sur le nombre de correspondances
        return produitsSimilaires.stream()
                .sorted(Comparator.comparing(p -> -produitIdToMatchCount.get(p.getId())))
                .collect(Collectors.toList());
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
        nouveauProduit.setNom(dto.getNomP());
        nouveauProduit.setPrixUnitaire(dto.getPrixUnitaireP());
        nouveauProduit.setPrixKg(dto.getPrixKgP());
        nouveauProduit.setPoids(dto.getPoidsP());
        nouveauProduit.setNutri(dto.getNutriP());
        nouveauProduit.setConditionnement(dto.getConditionnementP());
        nouveauProduit.setBio(dto.getBioP());
        nouveauProduit.setMarque(dto.getMarqueP());
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
    public ProduitDTO getProduitById(Integer id) {
        Produit produit = produitRepository.findByIdWithAssociations(id).orElse(null);
        if (produit == null) {
            return null;
        }

        // Charger les stockages séparément
        Produit produitWithStockages = produitRepository.findByIdWithStockages(id).orElse(null);
        if (produitWithStockages != null) {
            produit.setStockages(produitWithStockages.getStockages());
        }

        // Récupérer les produits similaires dans la même transaction
        List<Produit> produitsSimilaires = recommanderProduitsSimilaires(id);

        // Convertir en DTO pour éviter les problèmes de LazyInitialization
        return DTOMapper.convertToDTO(produit, produitsSimilaires);
    }
    
    @Transactional
    public Produit creerProduitDepuisFichier(MultipartFile file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NouveauProduitDTO dto = mapper.readValue(file.getInputStream(), NouveauProduitDTO.class);
        return creerProduit(dto);
    }

    @Transactional
    public List<Map<String, Object>> recommanderProduitsPourUtilisateur(int idUtilisateur) {
        List<Object[]> resultats = produitRepository.findTopProduitsForUser(idUtilisateur);

        return resultats.stream().map(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", r[0]);
            map.put("nom", r[1]);
            map.put("marque", r[2]);
            map.put("total_commandes", r[3]);
            return map;
        }).toList();
    }

}