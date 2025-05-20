package com.pickandgo.service;

import com.pickandgo.model.Produit;
import com.pickandgo.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    @Autowired
    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Integer id) {
        return produitRepository.findById(id).orElse(null);
    }
}