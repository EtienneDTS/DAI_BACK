package com.pickandgo.service;
import com.pickandgo.dto.SupprimerProduitEntierDTO;
import com.pickandgo.dto.SupprimerProduitPanierDTO;
import com.pickandgo.dto.AjoutProduitPanierDTO;
import com.pickandgo.model.*;
import com.pickandgo.repository.ConstituerRepository;
import com.pickandgo.repository.PanierRepository;
import com.pickandgo.repository.ProduitRepository;
import com.pickandgo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PanierService {

    private final PanierRepository panierRepository;
    private final ProduitRepository produitRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ConstituerRepository constituerRepository;

    @Autowired
    public PanierService(PanierRepository panierRepository,
                         ProduitRepository produitRepository,
                         UtilisateurRepository utilisateurRepository,
                         ConstituerRepository constituerRepository) {
        this.panierRepository = panierRepository;
        this.produitRepository = produitRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.constituerRepository = constituerRepository;
    }

    @Transactional
    public Panier ajouterProduitAuPanier(AjoutProduitPanierDTO dto) {
        // Vérifier si l'utilisateur existe
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getIdUtilisateur())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier si le produit existe
        Produit produit = produitRepository.findById(dto.getIdProduit())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Chercher un panier en cours pour cet utilisateur
        Panier panier;
        Optional<Panier> panierEnCours = panierRepository.findByUtilisateurIdAndStatus(
                utilisateur.getId(), Panier.StatutPanier.EN_COURS);

        if (panierEnCours.isEmpty()) {
            // Créer un nouveau panier
            panier = new Panier();
            panier.setUtilisateur(utilisateur);
            panier.setStatus(Panier.StatutPanier.EN_COURS);
            panier.setPrixtotalPa(BigDecimal.ZERO);
            panier = panierRepository.save(panier);
        } else {
            panier = panierEnCours.get();
        }

        // Chercher si ce produit est déjà dans le panier
        Optional<Constituer> ligneExistante = constituerRepository.findByPanier_IdPanierAndProduit_Id(
                panier.getIdPanier(), produit.getId());

        if (ligneExistante.isPresent()) {
            // Mettre à jour la quantité
            Constituer ligne = ligneExistante.get();
            ligne.setQuantite(ligne.getQuantite() + dto.getQuantite());
            constituerRepository.save(ligne);
        } else {
            // Ajouter le produit au panier
            Constituer nouvelleLigne = new Constituer();
            nouvelleLigne.setPanier(panier);
            nouvelleLigne.setProduit(produit);
            nouvelleLigne.setQuantite(dto.getQuantite());
            constituerRepository.save(nouvelleLigne);
        }

        // Mettre à jour le prix total du panier
        // (Vous devrez adapter cette partie selon votre logique de calcul)
        BigDecimal nouveauTotal = calculerPrixTotal(panier);
        panier.setPrixtotalPa(nouveauTotal);

        return panierRepository.save(panier);

    }

    // Méthode modifiée pour récupérer le panier d'un utilisateur (ou en créer un s'il n'existe pas)
    public Panier getPanierUtilisateur(Integer utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Optional<Panier> panierExistant = panierRepository.findByUtilisateurIdAndStatus(
                utilisateurId, Panier.StatutPanier.EN_COURS);

        if (panierExistant.isPresent()) {
            return panierExistant.get();
        } else {
            // Création automatique d'un panier si l'utilisateur n'en a pas
            Panier nouveauPanier = new Panier();
            nouveauPanier.setUtilisateur(utilisateur);
            nouveauPanier.setStatus(Panier.StatutPanier.EN_COURS);
            nouveauPanier.setPrixtotalPa(BigDecimal.ZERO);
            return panierRepository.save(nouveauPanier);
        }
    }

    @Transactional
    public Panier supprimerProduitEntier(SupprimerProduitEntierDTO dto) {
        Panier panier = panierRepository.findById(dto.getIdPanier())
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        // Trouver la ligne correspondant au produit
        Constituer ligne = null;
        for (Constituer l : panier.getLignes()) {
            if (l.getId().getProduitId().equals(dto.getIdProduit())) {
                ligne = l;
                break;
            }
        }

        if (ligne != null) {
            // Supprimer complètement la ligne
            panier.getLignes().remove(ligne);
            // Important: mettre à jour la référence du panier dans la ligne
            constituerRepository.delete(ligne);

            // Recalculer le prix total
            BigDecimal nouveauTotal = calculerPrixTotal(panier);
            panier.setPrixtotalPa(nouveauTotal);

            // Sauvegarder le panier après la modification
            return panierRepository.save(panier);
        } else {
            throw new RuntimeException("Le produit n'existe pas dans le panier");
        }
    }

    // Nouvelle méthode pour vider un panier (au lieu de le supprimer)
    @Transactional
    public Panier viderPanier(Integer panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        // Vérification optionnelle : on peut choisir de n'autoriser que de vider les paniers en cours
        if (panier.getStatus() != Panier.StatutPanier.EN_COURS) {
            throw new RuntimeException("Seuls les paniers en cours peuvent être vidés");
        }

        // Supprimer toutes les lignes du panier
        List<Constituer> lignes = constituerRepository.findByPanier_IdPanier(panierId);
        constituerRepository.deleteAll(lignes);

        // Réinitialiser le prix total
        panier.setPrixtotalPa(BigDecimal.ZERO);

        return panierRepository.save(panier);
    }

    @Transactional
    public Panier supprimerProduitDuPanier(SupprimerProduitPanierDTO dto) {
        Panier panier = panierRepository.findById(dto.getIdPanier())
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        // Trouver et supprimer la ligne directement au lieu de la manipuler en mémoire
        Constituer ligne = null;
        for (Constituer l : panier.getLignes()) {
            if (l.getId().getProduitId().equals(dto.getIdProduit())) {
                ligne = l;
                break;
            }
        }

        if (ligne != null) {
            if (dto.getQuantite() == null || dto.getQuantite() >= ligne.getQuantite()) {
                // Supprimer complètement la ligne
                panier.getLignes().remove(ligne);
                // Important: mettre à jour la référence du panier dans la ligne
                constituerRepository.delete(ligne);
            } else {
                // Réduire la quantité
                ligne.setQuantite(ligne.getQuantite() - dto.getQuantite());
                constituerRepository.save(ligne);
            }

            // Recalculer le prix total
            BigDecimal nouveauTotal = calculerPrixTotal(panier);
            panier.setPrixtotalPa(nouveauTotal);

            // Sauvegarder le panier après la modification
            return panierRepository.save(panier);
        }

        return panier;
    }

    private BigDecimal calculerPrixTotal(Panier panier) {
        BigDecimal total = BigDecimal.ZERO;
        for (Constituer ligne : panier.getLignes()) {
            BigDecimal prixLigne = ligne.getProduit().getPrixUnitaireP()
                    .multiply(BigDecimal.valueOf(ligne.getQuantite()));
            total = total.add(prixLigne);
        }
        return total;
    }

    @Transactional
    public Panier confirmerPanier(Integer panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        if (panier.getStatus() != Panier.StatutPanier.EN_COURS) {
            throw new RuntimeException("Ce panier ne peut pas être confirmé car il n'est pas en cours");
        }

        panier.setStatus(Panier.StatutPanier.CONFIRME);
        return panierRepository.save(panier);
    }

    @Transactional
    public Panier marquerCommeRecupere(Integer panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        if (panier.getStatus() != Panier.StatutPanier.CONFIRME) {
            throw new RuntimeException("Ce panier ne peut pas être marqué comme récupéré car il n'est pas confirmé");
        }

        panier.setStatus(Panier.StatutPanier.RECUPERE);
        return panierRepository.save(panier);
    }

}