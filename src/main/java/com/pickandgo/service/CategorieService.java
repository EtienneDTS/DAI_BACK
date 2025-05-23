package com.pickandgo.service;

import com.pickandgo.repository.CategorieRepository;
import com.pickandgo.model.Categorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;
    @Transactional
    public List<String> getAllCategorieNames() {
        return categorieRepository.findAll()
                .stream()
                .map(Categorie::getNomCate)
                .collect(Collectors.toList());
    }
}
