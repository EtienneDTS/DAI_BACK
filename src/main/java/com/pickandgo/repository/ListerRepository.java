package com.pickandgo.repository;

import com.pickandgo.model.Lister;
import com.pickandgo.model.ListerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListerRepository extends JpaRepository<Lister, ListerId> {
    // Supprimer un produit d'une liste
    void deleteByIdIdLAndIdIdP(Integer idL, Integer idP);

    // Vérifier si une ligne existe (utile si tu veux faire un test avant de modifier)
    boolean existsByIdIdLAndIdIdP(Integer idL, Integer idP);

    // Récupérer tous les produits d'une liste (utile si tu veux les afficher côté front)
    List<Lister> findByIdIdL(Integer idL);
}
