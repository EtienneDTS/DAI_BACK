package com.pickandgo.repository;

import com.pickandgo.model.Jour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourRepository extends JpaRepository<Jour, Integer> {
}