package com.pickandgo.repository;

import com.pickandgo.model.Produit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = """
    SELECT 
        p.idP,
        p.nomP,
        p.marqueP,
        SUM(c.quantiteP) AS total_commandes
    FROM Panier pa
    JOIN Constituer c ON pa.idPa = c.idPa
    JOIN Produit p ON c.idP = p.idP
    WHERE pa.idU = :idUtilisateur
    GROUP BY p.idP, p.nomP, p.marqueP
    ORDER BY total_commandes DESC
    LIMIT 5
    """, nativeQuery = true)
    List<Object[]> findTopProduitsForUser(@Param("idUtilisateur") Integer idUtilisateur);


    @Query(nativeQuery = true, value = """
    SELECT DISTINCT p.idP FROM Produit p
    LEFT JOIN Definir d ON p.idP = d.idP
    WHERE EXISTS (
        SELECT 1 FROM Definir d2
        WHERE d2.idP = p.idP AND d2.idMc IN :motsClesIds
    )
    AND p.idP != :produitId
    """)
    List<Integer> findSimilarProductIds(@Param("motsClesIds") List<Integer> motsClesIds,
                                        @Param("produitId") Integer produitId);

    default List<Produit> findSimilarProducts(List<Integer> motsClesIds, Integer produitId) {
        List<Integer> productIds = findSimilarProductIds(motsClesIds, produitId);
        return findAllById(productIds);
    }
    // Cette méthode peut être conservée pour compatibilité mais ne devrait plus être utilisée
    @Deprecated
    @EntityGraph(attributePaths = {"stockages"})
    List<Produit> findByMotsClesIdInAndIdNot(List<Integer> motCleIds, Integer produitId);
}