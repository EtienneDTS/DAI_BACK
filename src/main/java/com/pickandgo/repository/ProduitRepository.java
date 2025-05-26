package com.pickandgo.repository;

import com.pickandgo.model.Produit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Integer> {
    // Récupérer tous les produits avec leurs relations en une seule requête
    @Override
    @EntityGraph(attributePaths = {"motsCles", "rayon", "idCate"})
    List<Produit> findAll();

    // Récupérer un produit par ID avec toutes ses relations
    @Override
    @EntityGraph(attributePaths = {"motsCles", "rayon", "idCate"})
    Optional<Produit> findById(Integer id);

    List<Produit> findByMotsClesIdInAndIdNot(List<Integer> motCleIds, Integer produitId);

}