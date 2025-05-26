package com.pickandgo.repository;

import com.pickandgo.model.Stocker;
import com.pickandgo.model.StockerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StockerRepository extends JpaRepository<Stocker, StockerId> {

    @Query("SELECT s.quantite FROM Stocker s WHERE s.produit.id = :produitId AND s.magasin.id = :magasinId")
    Integer findQuantiteByProduitIdAndMagasinId(@Param("produitId") Integer produitId, @Param("magasinId") Integer magasinId);
}