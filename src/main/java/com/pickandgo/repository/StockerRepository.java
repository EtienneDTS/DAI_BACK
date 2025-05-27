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

    @Query("SELECT s.produit.id, s.quantite FROM Stocker s WHERE s.magasin.id = :magasinId AND s.produit.id IN :produitIds")
    List<Object[]> findQuantitesByProduitIdsAndMagasinId(@Param("produitIds") List<Integer> produitIds, @Param("magasinId") Integer magasinId);


}