package com.pickandgo.service;

import com.pickandgo.dto.NouveauProduitDTO;
import com.pickandgo.dto.ProduitDTO;
import com.pickandgo.mapper.DTOMapper;
import com.pickandgo.model.*;
import com.pickandgo.repository.CategorieRepository;
import com.pickandgo.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import com.pickandgo.repository.RayonRepository;
import com.pickandgo.repository.PromotionRepository;


import java.util.*;
import java.util.function.Function;
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


    @Transactional(readOnly = true)
    public List<ProduitDTO> getAllProduits() {
        // 1. Récupérer tous les produits avec leurs associations principales
        List<Produit> produits = produitRepository.findAllWithAssociations();

        if (produits.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Récupérer tous les IDs des produits pour charger les stockages
        List<Integer> produitIds = produits.stream()
                .map(Produit::getId)
                .collect(Collectors.toList());

        // 3. Charger les stockages pour tous les produits en une seule requête
        List<Produit> produitsAvecStockages = produitRepository.findAllByIdsWithStockages(produitIds);

        // 4. Créer une map des produits avec stockages pour un accès rapide
        Map<Integer, List<Stocker>> stockagesMap = produitsAvecStockages.stream()
                .filter(p -> p.getStockages() != null && !p.getStockages().isEmpty())
                .collect(Collectors.toMap(
                        Produit::getId,
                        Produit::getStockages
                ));

        // 5. Récupérer la map des produits similaires
        Map<Integer, List<Integer>> similarProductIdsMap = produitRepository.findAllSimilarProductIds();

        // 6. Construire les DTOs
        return produits.stream().map(produit -> {
            // Ajouter les stockages au produit
            if (stockagesMap.containsKey(produit.getId())) {
                produit.setStockages(stockagesMap.get(produit.getId()));
            }

            // Trouver les produits similaires
            List<Produit> similaires = new ArrayList<>();
            if (similarProductIdsMap.containsKey(produit.getId())) {
                List<Integer> similairesIds = similarProductIdsMap.get(produit.getId());
                similaires = produits.stream()
                        .filter(p -> similairesIds.contains(p.getId()))
                        .limit(3) // Limiter à 3 produits similaires
                        .collect(Collectors.toList());
            }

            // Convertir en DTO avec les produits similaires
            return DTOMapper.convertToDTO(produit, similaires);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProduitDTO getProduitById(Integer id) {
        // Récupérer le produit avec toutes ses associations en une seule requête
        Produit produit = produitRepository.findByIdWithAllAssociations(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + id));

        // Charger les produits similaires
        List<Produit> produitsSimilaires = recommanderProduitsSimilaires(id);

        // Convertir en DTO
        return DTOMapper.convertToDTO(produit, produitsSimilaires);
    }

    @Transactional(readOnly = true)
    public List<Produit> recommanderProduitsSimilaires(Integer idProduit) {
        int nombreRecommandations = 3;

        // Récupérer le produit de référence et ses mots-clés
        Produit produitReference = produitRepository.findById(idProduit)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id: " + idProduit));

        List<Integer> idMotsCles = produitReference.getMotsCles().stream()
                .map(MotCle::getId)
                .collect(Collectors.toList());

        if (idMotsCles.isEmpty()) {
            return Collections.emptyList();
        }

        // Récupérer les IDs des produits similaires en une seule requête
        List<Integer> produitSimilaireIds = produitRepository.findSimilarProductIds(idMotsCles, idProduit)
                .stream()
                .limit(nombreRecommandations)
                .collect(Collectors.toList());

        if (produitSimilaireIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Charger tous les produits similaires avec leurs détails complets
        return produitRepository.findAllByIdsWithStockages(produitSimilaireIds);
    }

    public Map<Integer, List<Produit>> findAllSimilarProducts(Map<Integer, List<Integer>> produitMotClesMap) {
        Map<Integer, List<Produit>> result = new HashMap<>();

        for (Map.Entry<Integer, List<Integer>> entry : produitMotClesMap.entrySet()) {
            Integer produitId = entry.getKey();
            List<Integer> motsClesIds = entry.getValue();

            // Limiter à 3 produits similaires
            List<Integer> similarProductIds = produitRepository.findSimilarProductIds(motsClesIds, produitId);
            List<Produit> similarProducts = produitRepository.findAllById(similarProductIds);

            if (!similarProducts.isEmpty()) {
                // Limiter à 3 produits maximum
                result.put(produitId, similarProducts.stream()
                        .limit(3)
                        .collect(Collectors.toList()));
            }
        }

        return result;
    }
    @Transactional(readOnly = true)
    public List<ProduitDTO> getAllProduitsWithDetails() {
        // 1. Récupérer tous les produits avec leurs associations en une seule requête
        List<Produit> produits = produitRepository.findAllWithAssociations();

        if (produits.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. Créer une map des produits par ID pour accès rapide
        Map<Integer, Produit> produitsMap = produits.stream()
                .collect(Collectors.toMap(Produit::getId, Function.identity()));

        // 3. Récupérer TOUS les produits similaires en une seule requête
        Map<Integer, List<Integer>> similarProductIdsMap = produitRepository.findAllSimilarProductIds();

        // 4. Construire les DTOs
        return produits.stream().map(produit -> {
            List<Integer> similarIds = similarProductIdsMap.getOrDefault(produit.getId(),
                    Collections.emptyList());

            List<Produit> produitsSimilaires = similarIds.stream()
                    .map(produitsMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return DTOMapper.convertToDTO(produit, produitsSimilaires);
        }).collect(Collectors.toList());
    }
    @Transactional
    public Produit creerProduitDepuisFichier(MultipartFile file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        NouveauProduitDTO dto = mapper.readValue(file.getInputStream(), NouveauProduitDTO.class);
        return creerProduit(dto);
    }
}