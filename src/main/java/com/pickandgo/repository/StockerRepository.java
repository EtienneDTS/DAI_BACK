package com.pickandgo.repository;

import com.pickandgo.model.Stocker;
import com.pickandgo.model.StockerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockerRepository extends JpaRepository<Stocker, StockerId> {

    @Query("SELECT s.quantite FROM Stocker s WHERE s.produit.id = :produitId AND s.magasin.id = :magasinId")
    Optional<Integer> findQuantiteByProduitIdAndMagasinId(@Param("produitId") Integer produitId, @Param("magasinId") Integer magasinId);

    @Query("""
        SELECT s.magasin.id
        FROM Stocker s
        JOIN Constituer c ON c.id.produitId = s.produit.id
        WHERE c.id.panierId = :idPanier
          AND s.quantite >= c.quantite
        GROUP BY s.magasin.id
        HAVING COUNT(s.produit.id) = (
            SELECT COUNT(c2.id.produitId)
            FROM Constituer c2
            WHERE c2.id.panierId = :idPanier
        )
    """)
    List<Integer> findMagasinsCompletsDisponibles(@Param("idPanier") Integer idPanier);
}