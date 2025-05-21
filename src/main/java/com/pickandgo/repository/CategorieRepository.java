package com.pickandgo.repository;

import com.pickandgo.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
    // Vous pouvez ajouter des méthodes spécifiques de requête ici si nécessaire
}