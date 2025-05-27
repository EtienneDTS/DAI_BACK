package com.pickandgo.service;

import com.pickandgo.model.Constituer;
import com.pickandgo.model.Magasin;
import com.pickandgo.model.Panier;
import com.pickandgo.repository.MagasinRepository;
import com.pickandgo.repository.PanierRepository;
import com.pickandgo.repository.StockerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MagasinService {

    @Autowired
    private MagasinRepository magasinRepository;

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private StockerRepository stockerRepository;

    public List<Magasin> getTousLesMagasins() {
        return magasinRepository.findAll();
    }

    public List<Integer> getMagasinsAvecDisponibiliteComplete(Integer idPanier, Integer magasinInitialId) {
        return stockerRepository.findMagasinsCompletsDisponibles(idPanier);
    }

    public List<Map<String, Integer>> getMagasinsAvecDisponibiliteCompleteFormatee(Integer idPanier, Integer magasinInitialId) {
        List<Integer> ids = getMagasinsAvecDisponibiliteComplete(idPanier, magasinInitialId);

        return ids.stream()
                .map(id -> {
                    Map<String, Integer> map = new HashMap<>();
                    map.put("idM", id);
                    return map;
                })
                .collect(Collectors.toList());
    }

}
