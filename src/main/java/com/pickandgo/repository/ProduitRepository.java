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


    @Query("SELECT DISTINCT p.id FROM Produit p " +
            "JOIN Definir d ON p.id = d.id.idP " +
            "WHERE d.id.idMc IN :motsClesIds " +
            "AND p.id != :produitId")
    List<Integer> findSimilarProductIds(@Param("motsClesIds") List<Integer> motsClesIds,
                                        @Param("produitId") Integer produitId);

    @Query("SELECT p FROM Produit p " +
            "LEFT JOIN FETCH p.stockages " +
            "LEFT JOIN FETCH p.motsCles " +
            "LEFT JOIN FETCH p.idCate " +
            "LEFT JOIN FETCH p.rayon " +
            "LEFT JOIN FETCH p.promotion " +
            "WHERE p.id IN :ids")
    List<Produit> findAllByIdWithAssociations(@Param("ids") List<Integer> ids);

    default List<Produit> findSimilarProducts(List<Integer> motsClesIds, Integer produitId) {
        List<Integer> productIds = findSimilarProductIds(motsClesIds, produitId);
        return findAllById(productIds);
    }
    // Cette méthode peut être conservée pour compatibilité mais ne devrait plus être utilisée
    @Deprecated
    @EntityGraph(attributePaths = {"stockages"})
    List<Produit> findByMotsClesIdInAndIdNot(List<Integer> motCleIds, Integer produitId);
}