package com.pickandgo.repository;

import com.pickandgo.model.PostIt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostItRepository extends JpaRepository<PostIt, Integer> {
}
