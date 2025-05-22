package com.pickandgo.repository;

import com.pickandgo.model.Lister;
import com.pickandgo.model.ListerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListerRepository extends JpaRepository<Lister, ListerId> {
    // Supprimer un produit d'une liste
    void deleteByIdIdLAndIdIdP(Integer idL, Integer idP);

    // Vérifier si une ligne existe
    boolean existsByIdIdLAndIdIdP(Integer idL, Integer idP);

    // Récupérer tous les produits d'une liste
    List<Lister> findByIdIdL(Integer idL);

    Optional<Lister> findByListeIdAndProduitId(Integer idListe, Integer idProduit);
}
