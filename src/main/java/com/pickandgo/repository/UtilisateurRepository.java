package com.pickandgo.repository;

import com.pickandgo.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByEmailU(String emailU);

    @Query("SELECT u FROM Utilisateur u JOIN FETCH u.magasin")
    List<Utilisateur> findAllWithMagasin();

}
