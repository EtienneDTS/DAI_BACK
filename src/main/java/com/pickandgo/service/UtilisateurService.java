package com.pickandgo.service;

import com.pickandgo.model.Magasin;
import com.pickandgo.model.Utilisateur;
import com.pickandgo.repository.UtilisateurRepository;
import com.pickandgo.repository.MagasinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;


    @Autowired  // <-- Ajout de l'injection
    private MagasinRepository magasinRepository;

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAllWithMagasin();
    }


    @Transactional
    public Utilisateur verifierConnexion(String email, String password) {
        return utilisateurRepository.findByEmailU(email)
                .filter(utilisateur -> utilisateur.getMotDePasse().equals(password))
                .orElse(null);
    }

    @Transactional
    public Map<String, Long> getRepartitionParTrancheAge() {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();

        return utilisateurs.stream()
                .filter(u -> u.getAgeU() != null)
                .collect(Collectors.groupingBy(
                        u -> getTranche(u.getAgeU()),
                        Collectors.counting()
                ));
    }

    private String getTranche(int age) {
        if (age <= 25) return "18-25";
        if (age <= 35) return "26-35";
        if (age <= 45) return "36-45";
        if (age <= 60) return "46-60";
        return "60+";
    }

    @Transactional
    public Utilisateur changerMagasinUtilisateur(Integer idUtilisateur, Integer idMagasin) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Magasin magasin = magasinRepository.findById(idMagasin)
                .orElseThrow(() -> new RuntimeException("Magasin non trouvé"));

        utilisateur.setMagasin(magasin);
        return utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public Utilisateur creerUtilisateurAnonymePourMagasin(Integer magasinId) {
        Magasin magasin = magasinRepository.findById(magasinId)
                .orElseThrow(() -> new RuntimeException("Magasin non trouvé avec l'ID: " + magasinId));

        // Génération d'un identifiant unique pour l'email anonyme
        String uniqueId = java.util.UUID.randomUUID().toString().substring(0, 8);

        Utilisateur utilisateurAnonyme = new Utilisateur();
        utilisateurAnonyme.setNomU("Anonyme");
        utilisateurAnonyme.setPrenomU("Anonyme");
        utilisateurAnonyme.setEmailU("anonyme_" + uniqueId + "@pickandgo.temp");
        utilisateurAnonyme.setRole("ANONYME");
        utilisateurAnonyme.setMotDePasse(java.util.UUID.randomUUID().toString());
        utilisateurAnonyme.setAdresseU("Adresse temporaire");
        utilisateurAnonyme.setMagasin(magasin);

        return utilisateurRepository.save(utilisateurAnonyme);
    }
}
