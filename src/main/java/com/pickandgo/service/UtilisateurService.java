package com.pickandgo.service;

import com.pickandgo.model.Utilisateur;
import com.pickandgo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Transactional
    public Utilisateur verifierConnexion(String email, String password) {
        return utilisateurRepository.findByEmailU(email)
                .filter(utilisateur -> utilisateur.getMotDePasse().equals(password))
                .orElse(null);
    }
}