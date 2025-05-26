package com.pickandgo.repository;

import com.pickandgo.model.ListeDeCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListeDeCourseRepository extends JpaRepository<ListeDeCourse, Integer> {
    List<ListeDeCourse> findByUtilisateurId(Integer utilisateurId);
    @Query("SELECT l FROM ListeDeCourse l " +
            "LEFT JOIN FETCH l.liaisonsProduits lp " +
            "LEFT JOIN FETCH lp.produit " +
            "WHERE l.id = :id")
    Optional<ListeDeCourse> findByIdWithProduits(@Param("id") Integer id);
}