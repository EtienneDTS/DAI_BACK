package com.pickandgo.repository;

import com.pickandgo.model.ListeDeCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListeDeCourseRepository extends JpaRepository<ListeDeCourse, Integer> {
    List<ListeDeCourse> findByUtilisateurId(Integer utilisateurId);
}