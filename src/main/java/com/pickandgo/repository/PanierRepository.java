package com.pickandgo.repository;

import com.pickandgo.model.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Integer> {
    Optional<Panier> findByUtilisateurIdAndStatus(Integer utilisateurId, Panier.StatutPanier status);

    @Query("SELECT DISTINCT p FROM Panier p " +
            "JOIN FETCH p.commandes c " +
            "WHERE c.idM.id = :magasinId AND p.status IN :statuts")
    List<Panier> findCommandesParMagasin(@Param("magasinId") Integer magasinId,
                                         @Param("statuts") List<Panier.StatutPanier> statuts);
}
