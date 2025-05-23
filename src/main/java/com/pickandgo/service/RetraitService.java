package com.pickandgo.service;

import com.pickandgo.dto.RetraitSelectionDTO;
import com.pickandgo.model.Disponible;
import com.pickandgo.repository.DisponibleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetraitService {

    @Autowired
    private DisponibleRepository disponibleRepository;

    public List<Disponible> getAvailableCreneauxByMagasinAndJour(Integer magasinId, Integer jourId) {
        return disponibleRepository.findAvailableCreneauxByMagasinAndJour(magasinId, jourId);
    }

    @Transactional
    public boolean reserveCreneau(RetraitSelectionDTO selection) {
        Disponible disponible = disponibleRepository.findByIdMIdAndIdDateIdAndIdCrId(
                selection.getMagasinId(),
                selection.getJourId(),
                selection.getCreneauId());

        if (disponible != null && Boolean.TRUE.equals(disponible.getDispo())) {
            disponible.setDispo(false);
            disponibleRepository.save(disponible);
            return true;
        }
        return false;
    }
}