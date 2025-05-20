package com.pickandgo.repository;

import com.pickandgo.model.Constituer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConstituerRepository extends JpaRepository<Constituer, Constituer.ConstituerPK> {
    Optional<Constituer> findByPanierIdAndProduitId(Integer panierId, Integer produitId);
    List<Constituer> findByPanierId(Integer panierId);
}