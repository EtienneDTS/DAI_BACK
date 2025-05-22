package com.pickandgo.service;

import com.pickandgo.model.Panier;
import com.pickandgo.model.Utilisateur;
import com.pickandgo.repository.PanierRepository;
import com.pickandgo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class UtilisateurAnonymeService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PanierRepository panierRepository;

    @Transactional
    public Utilisateur creerUtilisateurAnonyme(String sessionId) {
        Utilisateur utilisateurAnonyme = new Utilisateur();
        utilisateurAnonyme.setNomU("Anonyme");
        utilisateurAnonyme.setPrenomU("Anonyme");
        // Utilisation du sessionId pour faciliter la retrouvaille de l'utilisateur anonyme
        utilisateurAnonyme.setEmailU("anonyme_" + sessionId + "@pickandgo.temp");
        utilisateurAnonyme.setRole("ANONYME");
        utilisateurAnonyme.setMotDePasse(UUID.randomUUID().toString());
        utilisateurAnonyme.setAdresseU("Adresse temporaire");

        Utilisateur utilisateurSauvegarde = utilisateurRepository.save(utilisateurAnonyme);

        // Création automatique d'un panier pour cet utilisateur
        Panier panier = new Panier();
        panier.setUtilisateur(utilisateurSauvegarde);
        panier.setStatus(Panier.StatutPanier.PANIER);
        panier.setPrixtotalPa(BigDecimal.ZERO);
        panierRepository.save(panier);

        return utilisateurSauvegarde;
    }
}