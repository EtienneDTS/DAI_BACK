package com.pickandgo.service;

import com.pickandgo.dto.ModifierQuantiteProduitDTO;
import com.pickandgo.dto.SupprimerProduitEntierDTO;
import com.pickandgo.model.*;
import com.pickandgo.repository.ConstituerRepository;
import com.pickandgo.repository.PanierRepository;
import com.pickandgo.repository.ProduitRepository;
import com.pickandgo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PanierService {

    @Autowired
    private ConstituerRepository constituerRepository;

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurAnonymeService utilisateurAnonymeService;

    @Transactional
    public Panier modifierQuantiteProduit(ModifierQuantiteProduitDTO dto) {
        Panier panier = panierRepository.findById(dto.getIdPanier())
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        Produit produit = produitRepository.findById(dto.getIdProduit())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        Optional<Constituer> constituerOpt = constituerRepository.findByPanier_IdPanierAndProduit_Id(
                dto.getIdPanier(), dto.getIdProduit());

        Constituer ligne;

        if (constituerOpt.isPresent()) {
            // Produit déjà dans le panier, mise à jour de la quantité
            ligne = constituerOpt.get();
            if (dto.getNouvelleQuantite() <= 0) {
                // Si quantité <= 0, supprimer le produit du panier
                panier.getLignes().remove(ligne);
                constituerRepository.delete(ligne);
            } else {
                // Mise à jour de la quantité
                ligne.setQuantite(dto.getNouvelleQuantite());
                constituerRepository.save(ligne);
            }
        } else if (dto.getNouvelleQuantite() > 0) {
            // Nouveau produit à ajouter au panier
            ligne = new Constituer();
            ConstituerPK pk = new ConstituerPK();
            pk.setPanierId(panier.getIdPanier());
            pk.setProduitId(produit.getId());
            ligne.setId(pk);
            ligne.setPanier(panier);
            ligne.setProduit(produit);
            ligne.setQuantite(dto.getNouvelleQuantite());
            panier.getLignes().add(ligne);
            constituerRepository.save(ligne);
        }

        // Recalcul du prix total du panier
        mettreAJourPrixTotalPanier(panier);

        return panierRepository.save(panier);
    }

    @Transactional
    public Panier supprimerProduitEntier(SupprimerProduitEntierDTO dto) {
        Panier panier = panierRepository.findById(dto.getIdPanier())
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        Optional<Constituer> constituerOpt = constituerRepository.findByPanier_IdPanierAndProduit_Id(
                dto.getIdPanier(), dto.getIdProduit());

        if (constituerOpt.isPresent()) {
            Constituer ligne = constituerOpt.get();
            panier.getLignes().remove(ligne);
            constituerRepository.delete(ligne);

            // Recalcul du prix total du panier
            mettreAJourPrixTotalPanier(panier);

            return panierRepository.save(panier);
        } else {
            throw new RuntimeException("Produit non trouvé dans le panier");
        }
    }

    @Transactional
    public void mettreAJourPrixTotalPanier(Panier panier) {
        panier.setPrixtotalPa(calculerPrixTotal(panier));
    }

    // Nouvelle méthode pour vider un panier
    @Transactional
    public Panier viderPanier(Integer panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        // Vérification optionnelle : on peut choisir de n'autoriser que de vider les paniers en cours
        if (panier.getStatus() != Panier.StatutPanier.PANIER) {
            throw new RuntimeException("Seuls les paniers en cours peuvent être vidés");
        }

        // Supprimer toutes les lignes du panier
        List<Constituer> lignes = constituerRepository.findByPanier_IdPanier(panierId);
        constituerRepository.deleteAll(lignes);
        panier.getLignes().clear();

        // Réinitialiser le prix total
        panier.setPrixtotalPa(BigDecimal.ZERO);

        return panierRepository.save(panier);
    }
    @Transactional
    public BigDecimal calculerPrixTotal(Panier panier) {
        BigDecimal total = BigDecimal.ZERO;
        for (Constituer ligne : panier.getLignes()) {
            BigDecimal prixLigne = ligne.getProduit().getPrixUnitaireP()
                    .multiply(BigDecimal.valueOf(ligne.getQuantite()));
            total = total.add(prixLigne);
        }
        return total;
    }

    @Transactional
    public Panier modifierQuantiteProduitAnonyme(ModifierQuantiteProduitDTO dto, String sessionId) {
        // Vérifier si un utilisateur anonyme existe déjà pour cette session
        Optional<Utilisateur> utilisateurAnonymeOpt = utilisateurRepository.findByEmailU(
                "anonyme_" + sessionId + "@pickandgo.temp");

        Utilisateur utilisateurAnonyme;
        if (!utilisateurAnonymeOpt.isPresent()) {
            // Créer un nouvel utilisateur anonyme et son panier
            utilisateurAnonyme = utilisateurAnonymeService.creerUtilisateurAnonyme(sessionId);
        } else {
            utilisateurAnonyme = utilisateurAnonymeOpt.get();
        }

        // Remplacer par la méthode correcte
        Optional<Panier> panierOpt = panierRepository.findByUtilisateurIdAndStatus(
                utilisateurAnonyme.getId(), Panier.StatutPanier.PANIER);

        if (!panierOpt.isPresent()) {
            throw new RuntimeException("Panier non trouvé pour cet utilisateur anonyme");
        }

        // Modifier le dto pour utiliser l'ID du panier anonyme
        dto.setIdPanier(panierOpt.get().getIdPanier());

        // Appeler la méthode existante pour modifier la quantité
        return modifierQuantiteProduit(dto);
    }

    @Transactional
    public void fusionnerPanierAnonymeVersUtilisateur(String sessionId, Integer utilisateurId) {
        // Vérifier si un utilisateur anonyme existe pour cette session
        Optional<Utilisateur> utilisateurAnonymeOpt = utilisateurRepository.findByEmailU(
                "anonyme_" + sessionId + "@pickandgo.temp");

        if (!utilisateurAnonymeOpt.isPresent()) {
            // Pas d'utilisateur anonyme, rien à fusionner
            return;
        }

        Utilisateur utilisateurAnonyme = utilisateurAnonymeOpt.get();

        // Récupérer le panier de l'utilisateur anonyme
        Optional<Panier> panierAnonymeOpt = panierRepository.findByUtilisateurIdAndStatus(
                utilisateurAnonyme.getId(), Panier.StatutPanier.PANIER);

        if (!panierAnonymeOpt.isPresent() || panierAnonymeOpt.get().getLignes().isEmpty()) {
            // Panier non trouvé ou vide, simplement supprimer l'utilisateur anonyme
            utilisateurRepository.delete(utilisateurAnonyme);
            return;
        }

        Panier panierAnonyme = panierAnonymeOpt.get();

        // Récupérer ou créer le panier de l'utilisateur connecté
        Optional<Panier> panierUtilisateurOpt = panierRepository.findByUtilisateurIdAndStatus(
                utilisateurId, Panier.StatutPanier.PANIER);

        Panier panierUtilisateur;
        if (!panierUtilisateurOpt.isPresent()) {
            // Créer un nouveau panier pour l'utilisateur connecté
            Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur connecté non trouvé"));

            panierUtilisateur = new Panier();
            panierUtilisateur.setUtilisateur(utilisateur);
            panierUtilisateur.setStatus(Panier.StatutPanier.PANIER);
            panierUtilisateur.setPrixtotalPa(BigDecimal.ZERO);
            panierRepository.save(panierUtilisateur);
        } else {
            panierUtilisateur = panierUtilisateurOpt.get();
        }

        // Transférer les produits du panier anonyme vers le panier de l'utilisateur connecté
        for (Constituer ligne : panierAnonyme.getLignes()) {
            ModifierQuantiteProduitDTO dto = new ModifierQuantiteProduitDTO();
            dto.setIdPanier(panierUtilisateur.getIdPanier());
            dto.setIdProduit(ligne.getProduit().getId());

            // Vérifier si le produit existe déjà dans le panier utilisateur
            Optional<Constituer> ligneUtilisateurOpt = constituerRepository.findByPanier_IdPanierAndProduit_Id(
                    panierUtilisateur.getIdPanier(), ligne.getProduit().getId());

            if (ligneUtilisateurOpt.isPresent()) {
                // Ajouter les quantités
                dto.setNouvelleQuantite(ligneUtilisateurOpt.get().getQuantite() + ligne.getQuantite());
            } else {
                // Ajouter le produit avec la même quantité
                dto.setNouvelleQuantite(ligne.getQuantite());
            }

            modifierQuantiteProduit(dto);
        }

        // Supprimer les lignes du panier anonyme
        List<Constituer> lignes = constituerRepository.findByPanier_IdPanier(panierAnonyme.getIdPanier());
        constituerRepository.deleteAll(lignes);

        // Supprimer le panier anonyme
        panierRepository.delete(panierAnonyme);

        // Supprimer l'utilisateur anonyme
        utilisateurRepository.delete(utilisateurAnonyme);
    }

    // Méthode pour finaliser une commande après connexion
    @Transactional
    public Panier finaliserCommandeApresConnexion(Integer panierAnonymeId, Integer utilisateurId) {
        // 1. Récupérer le panier anonyme
        Panier panierAnonyme = panierRepository.findById(panierAnonymeId)
                .orElseThrow(() -> new RuntimeException("Panier anonyme non trouvé"));

        // 2. Récupérer ou créer le panier de l'utilisateur connecté
        Optional<Panier> panierUtilisateurOpt = panierRepository.findByUtilisateurIdAndStatus(
                utilisateurId, Panier.StatutPanier.PANIER);

        Panier panierUtilisateur;
        if (!panierUtilisateurOpt.isPresent()) {
            // Créer un nouveau panier pour l'utilisateur connecté
            Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur connecté non trouvé"));

            panierUtilisateur = new Panier();
            panierUtilisateur.setUtilisateur(utilisateur);
            panierUtilisateur.setStatus(Panier.StatutPanier.PANIER);
            panierUtilisateur.setPrixtotalPa(BigDecimal.ZERO);
            panierRepository.save(panierUtilisateur);
        } else {
            panierUtilisateur = panierUtilisateurOpt.get();
        }

        // 3. Transférer tous les produits du panier anonyme vers le panier utilisateur
        for (Constituer ligne : panierAnonyme.getLignes()) {
            // Ajouter ou fusionner chaque produit dans le panier utilisateur
            ModifierQuantiteProduitDTO dto = new ModifierQuantiteProduitDTO();
            dto.setIdPanier(panierUtilisateur.getIdPanier());
            dto.setIdProduit(ligne.getProduit().getId());

            // Vérifier si le produit existe déjà dans le panier utilisateur
            Optional<Constituer> ligneUtilisateurOpt = constituerRepository.findByPanier_IdPanierAndProduit_Id(
                    panierUtilisateur.getIdPanier(), ligne.getProduit().getId());

            if (ligneUtilisateurOpt.isPresent()) {
                // Ajouter les quantités
                dto.setNouvelleQuantite(ligneUtilisateurOpt.get().getQuantite() + ligne.getQuantite());
            } else {
                // Ajouter le produit avec la même quantité
                dto.setNouvelleQuantite(ligne.getQuantite());
            }

            modifierQuantiteProduit(dto);
        }

        // 4. Supprimer les lignes du panier anonyme puis le panier anonyme
        List<Constituer> lignes = constituerRepository.findByPanier_IdPanier(panierAnonyme.getIdPanier());
        constituerRepository.deleteAll(lignes);
        panierRepository.delete(panierAnonyme);

        // 5. Supprimer l'utilisateur anonyme
        Utilisateur utilisateurAnonyme = panierAnonyme.getUtilisateur();
        if (utilisateurAnonyme != null && utilisateurAnonyme.getEmailU().startsWith("anonyme_")) {
            utilisateurRepository.delete(utilisateurAnonyme);
        }

        // 6. Passer le panier utilisateur en commande
        return passerCommande(panierUtilisateur.getIdPanier());
    }

    // Méthode pour passer une commande avec authentification
    @Transactional
    public Panier passerCommande(Integer panierId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Vous devez vous connecter pour passer commande");
        }

        // Récupérer l'utilisateur depuis l'authentification
        String username = authentication.getName();
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmailU(username);

        if (!utilisateurOpt.isPresent()) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        // Vérifier que le panier appartient bien à l'utilisateur
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        if (panier.getUtilisateur() != null && !panier.getUtilisateur().getId().equals(utilisateur.getId())) {
            throw new RuntimeException("Ce panier ne vous appartient pas");
        }

        // Associer l'utilisateur au panier si ce n'est pas déjà fait
        if (panier.getUtilisateur() == null) {
            panier.setUtilisateur(utilisateur);
            panierRepository.save(panier);
        }

        // Continuer avec la logique normale de commande
        return passerCommande(panierId);
    }

    // Méthode existante améliorée
    @Transactional
    public Panier passerCommande(Integer panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        // Vérifier que le panier n'est pas vide
        if (panier.getLignes().isEmpty()) {
            throw new RuntimeException("Impossible de passer une commande avec un panier vide");
        }

        // Passer à l'état COMMANDE
        panier.setStatus(Panier.StatutPanier.COMMANDE);

        // Ajouter la date de commande si un champ existe pour cela
        // Comme ce champ n'est pas présent dans le modèle fourni, cette ligne est commentée
        // panier.setDateCommande(LocalDateTime.now());

        return panierRepository.save(panier);
    }

    @Transactional
    public Panier demarrerPreparation(Integer panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        if (panier.getStatus() != Panier.StatutPanier.COMMANDE) {
            throw new RuntimeException("Ce panier ne peut pas être préparé car il n'est pas au statut COMMANDE");
        }

        panier.setStatus(Panier.StatutPanier.EN_PREPARATION);
        return panierRepository.save(panier);
    }

    @Transactional
    public Panier terminerPreparation(Integer panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        if (panier.getStatus() != Panier.StatutPanier.EN_PREPARATION) {
            throw new RuntimeException("Ce panier ne peut pas être marqué comme prêt car il n'est pas en préparation");
        }

        panier.setStatus(Panier.StatutPanier.PRET);
        return panierRepository.save(panier);
    }

    @Transactional
    public Panier marquerCommeRecupere(Integer panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        if (panier.getStatus() != Panier.StatutPanier.PRET) {
            throw new RuntimeException("Ce panier ne peut pas être marqué comme récupéré car il n'est pas prêt");
        }

        panier.setStatus(Panier.StatutPanier.RECUPERE);
        return panierRepository.save(panier);
    }

    // Si cette méthode est utilisée ailleurs, la conserver
    @Transactional
    public Panier getPanierUtilisateur(Integer userId) {
        // Rechercher un panier existant au statut PANIER pour cet utilisateur
        Optional<Panier> panierOptional = panierRepository.findByUtilisateurIdAndStatus(
                userId, Panier.StatutPanier.PANIER);

        return panierOptional.orElseThrow(() -> new RuntimeException("Panier non trouvé pour cet utilisateur"));
    }
}