package com.pickandgo.repository;

import com.pickandgo.model.Constituer;
import com.pickandgo.model.Panier;
import com.pickandgo.model.ConstituerPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ConstituerRepository extends JpaRepository<Constituer, ConstituerPK> {
    Optional<Constituer> findByPanier_IdPanierAndProduit_Id(Integer IdPanier, Integer id);
    List<Constituer> findByPanier_IdPanier(Integer panierId);
}