package com.pickandgo.service;

import com.pickandgo.model.Constituer;
import com.pickandgo.model.Magasin;
import com.pickandgo.model.Panier;
import com.pickandgo.repository.MagasinRepository;
import com.pickandgo.repository.StockerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MagasinService {

    @Autowired
    private MagasinRepository magasinRepository;

    @Autowired
    private StockerRepository stockerRepository;

    @Autowired
    private PanierService panierService;

    public List<Magasin> getTousLesMagasins() {
        return magasinRepository.findAll();
    }

    //TEST SO POUR DISPO MAGASIN
    public List<Integer> getMagasinsAvecDisponibiliteComplete(Integer userId, Integer magasinInitialId) {
        Panier panier = panierService.getPanierUtilisateurAvecDisponibilite(userId, magasinInitialId);

        Map<Integer, Integer> quantitesVoulues = panier.getLignes().stream()
                .collect(Collectors.toMap(
                        ligne -> ligne.getProduit().getId(),
                        Constituer::getQuantite
                ));

        boolean auMoinsUnIndisponible = panier.getLignes().stream()
                .anyMatch(ligne -> !Boolean.TRUE.equals(ligne.getDispo()));

        if (!auMoinsUnIndisponible) {
            return Collections.emptyList(); // Tout est dispo
        }

        List<Magasin> autresMagasins = magasinRepository.findAll().stream()
                .filter(m -> !m.getId().equals(magasinInitialId))
                .toList();

        List<Integer> magasinsDisponibles = new ArrayList<>();

        for (Magasin magasin : autresMagasins) {
            List<Object[]> stock = stockerRepository.findQuantitesByProduitIdsAndMagasinId(
                    new ArrayList<>(quantitesVoulues.keySet()), magasin.getId());

            Map<Integer, Integer> stockMap = stock.stream()
                    .collect(Collectors.toMap(
                            row -> (Integer) row[0],
                            row -> (Integer) row[1]
                    ));

            boolean tousDisponibles = quantitesVoulues.entrySet().stream()
                    .allMatch(entry -> stockMap.getOrDefault(entry.getKey(), 0) >= entry.getValue());

            if (tousDisponibles) {
                magasinsDisponibles.add(magasin.getId());
            }
        }

        return magasinsDisponibles;
    }
}
