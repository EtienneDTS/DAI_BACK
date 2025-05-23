package com.pickandgo.repository;

import com.pickandgo.model.Creneau;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreneauRepository extends JpaRepository<Creneau, Integer> {
}