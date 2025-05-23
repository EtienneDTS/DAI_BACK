package com.pickandgo.repository;

import com.pickandgo.model.Disponible;
import com.pickandgo.model.DisponibleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibleRepository extends JpaRepository<Disponible, DisponibleId> {
    @Query("SELECT d FROM Disponible d WHERE d.idM.id = :magasinId AND d.idDate.id = :jourId AND d.dispo = true")
    List<Disponible> findAvailableCreneauxByMagasinAndJour(Integer magasinId, Integer jourId);

    Disponible findByIdMIdAndIdDateIdAndIdCrId(Integer magasinId, Integer jourId, Integer creneauId);
}