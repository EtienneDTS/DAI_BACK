package com.pickandgo.repository;

import com.pickandgo.model.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Integer> {
    Optional<Panier> findByUtilisateurIdAndStatus(Integer utilisateurId, Panier.StatutPanier status);
}