package com.pickandgo.service;

import com.pickandgo.dto.NouveauProduitDTO;
import com.pickandgo.model.Categorie;
import com.pickandgo.model.MotCle;
import com.pickandgo.model.Produit;
import com.pickandgo.repository.CategorieRepository;
import com.pickandgo.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<Produit> recommanderProduitsSimilaires(Integer idProduit) {
        int nombreRecommandations =3;
        // Récupérer le produit de référence
        Produit produitReference = produitRepository.findById(idProduit)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id: " + idProduit));

        // Récupérer les mots-clés du produit de référence
        List<MotCle> motsClesProduit = produitReference.getMotsCles();

        if (motsClesProduit.isEmpty()) {
            return Collections.emptyList();
        }

        // Extraire les IDs des mots-clés
        List<Integer> idMotsCles = motsClesProduit.stream()
                .map(MotCle::getId)
                .collect(Collectors.toList());

        // Trouver tous les produits qui partagent au moins un mot-clé avec le produit de référence
        // mais ne sont pas le produit lui-même
        List<Produit> produitsSimilaires = produitRepository.findByMotsClesIdInAndIdNot(idMotsCles, idProduit);

        // Calculer le score de similarité pour chaque produit (nombre de mots-clés en commun)
        Map<Produit, Long> scoresSimilarite = produitsSimilaires.stream()
                .collect(Collectors.toMap(
                        produit -> produit,
                        produit -> produit.getMotsCles().stream()
                                .filter(mc -> idMotsCles.contains(mc.getId()))
                                .count()
                ));

        // Trier les produits par score de similarité décroissant et limiter le nombre de recommandations
        return scoresSimilarite.entrySet().stream()
                .sorted(Map.Entry.<Produit, Long>comparingByValue().reversed())
                .limit(nombreRecommandations)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
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

    @Transactional
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    @Transactional
    public Produit getProduitById(Integer id) {
        return produitRepository.findById(id).orElse(null);
    }
}