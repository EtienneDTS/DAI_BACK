package com.pickandgo.service;

import com.pickandgo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public boolean verifierConnexion(String email, String password) {
        return utilisateurRepository.findByEmailU(email)
                .map(utilisateur -> utilisateur.getPassword().equals(password))
                .orElse(false);
    }
}