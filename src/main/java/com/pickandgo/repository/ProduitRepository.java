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
    @EntityGraph(attributePaths = {"motsCles", "rayon", "idCate", "stockages", "stockages.magasin"})
    Optional<Produit> findById(Integer id);

    @Query(nativeQuery = true, value = """
    SELECT DISTINCT p.*, cat.*, r.*, promo.*, s.*, m.*
    FROM Produit p
    LEFT JOIN Categorie cat ON p.idCate = cat.idCate
    LEFT JOIN Rayon r ON p.idR = r.idR
    LEFT JOIN Promotion promo ON p.idPr = promo.idPr
    LEFT JOIN Stocker s ON p.idP = s.idP
    LEFT JOIN Magasin m ON s.idM = m.idM
    LEFT JOIN Definir d ON p.idP = d.idP
    LEFT JOIN MotCle mc ON d.idMc = mc.idMc
    WHERE EXISTS (
        SELECT 1 FROM Definir d2
        WHERE d2.idP = p.idP AND d2.idMc IN :motsClesIds
    )
    AND p.idP != :produitId
""")
    List<Object[]> findSimilarProductsNative(@Param("motsClesIds") List<Integer> motsClesIds,
                                             @Param("produitId") Integer produitId);
    // Rechercher des produits par catégorie avec toutes les relations importantes
    @Query("SELECT DISTINCT p FROM Produit p " +
            "LEFT JOIN FETCH p.motsCles " +
            "LEFT JOIN FETCH p.stockages s " +
            "LEFT JOIN FETCH s.magasin " +
            "WHERE p.idCate.id = :categorieId")
    List<Produit> findByCategorie(@Param("categorieId") Integer categorieId);

    // Rechercher des produits par rayon avec toutes les relations importantes
    @Query("SELECT DISTINCT p FROM Produit p " +
            "LEFT JOIN FETCH p.motsCles " +
            "LEFT JOIN FETCH p.stockages s " +
            "LEFT JOIN FETCH s.magasin " +
            "WHERE p.rayon.id = :rayonId")
    List<Produit> findByRayon(@Param("rayonId") Integer rayonId);

    // Rechercher des produits bio avec toutes les relations importantes
    @Query("SELECT DISTINCT p FROM Produit p " +
            "LEFT JOIN FETCH p.motsCles " +
            "LEFT JOIN FETCH p.stockages s " +
            "LEFT JOIN FETCH s.magasin " +
            "WHERE p.bioP = true")
    List<Produit> findBioProducts();

    // Rechercher des produits par nom (recherche partielle)
    @Query("SELECT DISTINCT p FROM Produit p " +
            "LEFT JOIN FETCH p.motsCles " +
            "LEFT JOIN FETCH p.stockages s " +
            "LEFT JOIN FETCH s.magasin " +
            "WHERE LOWER(p.nomP) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Produit> findByNomContainingIgnoreCase(@Param("nom") String nom);

    // Cette méthode peut être conservée pour compatibilité mais ne devrait plus être utilisée
    @Deprecated
    @EntityGraph(attributePaths = {"stockages"})
    List<Produit> findByMotsClesIdInAndIdNot(List<Integer> motCleIds, Integer produitId);
}