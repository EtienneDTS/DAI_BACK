// CommanderRepository.java
package com.pickandgo.repository;

import com.pickandgo.model.Commander;
import com.pickandgo.model.CommanderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommanderRepository extends JpaRepository<Commander, CommanderId> {

}