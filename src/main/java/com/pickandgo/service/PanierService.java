package com.pickandgo.service;

import com.pickandgo.dto.ModifierQuantiteProduitDTO;
import com.pickandgo.dto.RetraitSelectionDTO;
import com.pickandgo.dto.SupprimerProduitEntierDTO;
import com.pickandgo.model.*;
import com.pickandgo.repository.*;
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

    @Autowired
    private DisponibleRepository disponibleRepository;

    @Autowired
    private MagasinRepository magasinRepository;

    @Autowired
    private JourRepository jourRepository;

    @Autowired
    private CreneauRepository creneauRepository;

    @Autowired
    private CommanderRepository commanderRepository;

    @Autowired
    private StockerRepository stockerRepository;


    @Transactional
    public Panier ajouterProduitAuPanierUtilisateur(Integer idUtilisateur, Integer idProduit, Integer quantite) {
        // Trouver ou créer le panier de l'utilisateur
        Optional<Panier> panierOpt = panierRepository.findByUtilisateurIdAndStatus(
                idUtilisateur, Panier.StatutPanier.PANIER);

        Panier panier;
        if (!panierOpt.isPresent()) {
            // Créer un nouveau panier pour l'utilisateur
            Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            panier = new Panier();
            panier.setUtilisateur(utilisateur);
            panier.setStatus(Panier.StatutPanier.PANIER);
            panier.setPrixtotalPa(BigDecimal.ZERO);
            panier = panierRepository.save(panier);
        } else {
            panier = panierOpt.get();
        }

        // Vérifier que le produit existe
        Produit produit = produitRepository.findById(idProduit)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Créer le DTO pour utiliser la méthode existante
        ModifierQuantiteProduitDTO dto = new ModifierQuantiteProduitDTO();
        dto.setIdPanier(panier.getIdPanier());
        dto.setIdProduit(idProduit);

        // Vérifier si le produit est déjà dans le panier
        Optional<Constituer> ligneOpt = constituerRepository.findByPanier_IdPanierAndProduit_Id(
                panier.getIdPanier(), idProduit);

        if (ligneOpt.isPresent()) {
            // Ajouter à la quantité existante
            dto.setNouvelleQuantite(ligneOpt.get().getQuantite() + quantite);
        } else {
            // Nouvelle quantité
            dto.setNouvelleQuantite(quantite);
        }

        // Utiliser la méthode existante pour ajouter le produit
        return modifierQuantiteProduit(dto);
    }

    @Transactional
    public Panier passerCommandeAvecRetrait(Integer panierId, RetraitSelectionDTO selection) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        // Vérifier la disponibilité du créneau
        Disponible disponible = disponibleRepository.findByIdMIdAndIdDateIdAndIdCrId(
                selection.getMagasinId(), selection.getJourId(), selection.getCreneauId());
        if (disponible == null || !Boolean.TRUE.equals(disponible.getDispo())) {
            throw new RuntimeException("Créneau non disponible");
        }
        disponible.setDispo(false);
        disponibleRepository.save(disponible);

        // Créer l'entrée Commander
        Commander commander = new Commander();
        CommanderId commanderId = new CommanderId();
        commanderId.setIdPa(panierId);
        commanderId.setIdM(selection.getMagasinId());
        commander.setId(commanderId);


        Magasin magasin = magasinRepository.findById(selection.getMagasinId())
                .orElseThrow(() -> new RuntimeException("Magasin non trouvé"));
        Jour jour = jourRepository.findById(selection.getJourId())
                .orElseThrow(() -> new RuntimeException("Jour non trouvé"));
        Creneau creneau = creneauRepository.findById(selection.getCreneauId())
                .orElseThrow(() -> new RuntimeException("Créneau non trouvé"));

        commander.setIdPa(panier);
        commander.setIdM(magasin);
        commander.setDateC(jour.getDateJour());
        commander.setCreneauChoisi(creneau.getNom());

        commanderRepository.save(commander);

        // Passer le panier au statut COMMANDE
        panier.setStatus(Panier.StatutPanier.COMMANDE);
        return panierRepository.save(panier);
    }

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
            BigDecimal prixLigne = ligne.getProduit().getPrixUnitaire()
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

    @Transactional
    public Panier choisirRetraitEtReserverCreneau(Integer panierId, RetraitSelectionDTO selection) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        // Vérifier la disponibilité du créneau
        Disponible disponible = disponibleRepository.findByIdMIdAndIdDateIdAndIdCrId(
                selection.getMagasinId(), selection.getJourId(), selection.getCreneauId());

        if (disponible == null || !Boolean.TRUE.equals(disponible.getDispo())) {
            throw new RuntimeException("Créneau non disponible");
        }

        // Réserver le créneau
        disponible.setDispo(false);
        disponibleRepository.save(disponible);

        // (Optionnel) Associer le créneau au panier (il faut ajouter les champs dans Panier)
        // panier.setMagasin(...);
        // panier.setJour(...);
        // panier.setCreneau(...);

        return panierRepository.save(panier);
    }

    // Méthode pour finaliser une commande après connexion
    @Transactional
    public Panier finaliserCommandeApresConnexion(Integer panierAnonymeId, Integer utilisateurId) {
        // 1. Récupérer le panier anonyme
        Panier panierAnonyme = panierRepository.findById(panierAnonymeId)
                .orElseThrow(() -> new RuntimeException("Panier anonyme non trouvé"));

        // 2. Récupérer ou créer le panier utilisateur
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
            ModifierQuantiteProduitDTO dto = new ModifierQuantiteProduitDTO();
            dto.setIdPanier(panierUtilisateur.getIdPanier());
            dto.setIdProduit(ligne.getProduit().getId());

            Optional<Constituer> ligneUtilisateurOpt = constituerRepository.findByPanier_IdPanierAndProduit_Id(
                    panierUtilisateur.getIdPanier(), ligne.getProduit().getId());

            if (ligneUtilisateurOpt.isPresent()) {
                dto.setNouvelleQuantite(ligneUtilisateurOpt.get().getQuantite() + ligne.getQuantite());
            } else {
                dto.setNouvelleQuantite(ligne.getQuantite());
            }

            modifierQuantiteProduit(dto);
        }

        // 4. Supprimer le panier anonyme et l'utilisateur anonyme
        List<Constituer> lignes = constituerRepository.findByPanier_IdPanier(panierAnonyme.getIdPanier());
        constituerRepository.deleteAll(lignes);
        panierRepository.delete(panierAnonyme);

        Utilisateur utilisateurAnonyme = panierAnonyme.getUtilisateur();
        if (utilisateurAnonyme != null && utilisateurAnonyme.getEmailU().startsWith("anonyme_")) {
            utilisateurRepository.delete(utilisateurAnonyme);
        }

        // 5. Créer une commande à partir du panier utilisateur
        return panierUtilisateur;
    }

    @Transactional
    public Panier creerPanierPourUtilisateur(Integer utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Panier panier = new Panier();
        panier.setUtilisateur(utilisateur);
        panier.setStatus(Panier.StatutPanier.PANIER); // Supposant que cette enum existe
        panier.setPrixtotalPa(BigDecimal.ZERO);

        return panierRepository.save(panier);
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

        // Continuer avec la logique nouvelle de commande
        return passerCommande(panierId);
    }

    @Transactional
    public Panier passerCommande(Integer panierId) {
        // Récupérer le panier d'origine
        Panier panierOriginal = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        // Vérifier que le panier n'est pas vide
        if (panierOriginal.getLignes().isEmpty()) {
            throw new RuntimeException("Impossible de passer une commande avec un panier vide");
        }

        // Créer un nouveau panier pour la commande
        Panier panierCommande = new Panier();
        panierCommande.setUtilisateur(panierOriginal.getUtilisateur());
        panierCommande.setStatus(Panier.StatutPanier.COMMANDE);
        panierCommande.setPrixtotalPa(panierOriginal.getPrixtotalPa());

        // Sauvegarder le nouveau panier
        panierCommande = panierRepository.save(panierCommande);

        // Copier les produits du panier original vers le panier commande
        for (Constituer ligneOriginal : panierOriginal.getLignes()) {
            Constituer ligneCommande = new Constituer();
            ConstituerPK pkCommande = new ConstituerPK();
            pkCommande.setPanierId(panierCommande.getIdPanier());
            pkCommande.setProduitId(ligneOriginal.getProduit().getId());

            ligneCommande.setId(pkCommande);
            ligneCommande.setPanier(panierCommande);
            ligneCommande.setProduit(ligneOriginal.getProduit());
            ligneCommande.setQuantite(ligneOriginal.getQuantite());

            constituerRepository.save(ligneCommande);
            panierCommande.getLignes().add(ligneCommande);
        }

        // Vider le panier original (supprimer toutes les lignes)
        List<Constituer> lignes = constituerRepository.findByPanier_IdPanier(panierOriginal.getIdPanier());
        constituerRepository.deleteAll(lignes);
        panierOriginal.getLignes().clear();
        panierOriginal.setPrixtotalPa(BigDecimal.ZERO);
        panierRepository.save(panierOriginal);

        return panierCommande;
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


    //Pour ajouter liste à panier
    @Transactional
    public Panier ajouterProduitsDepuisListeDansPanier(Integer idUtilisateur, List<Lister> liaisonsProduits) {
        Panier panier = panierRepository.findByUtilisateurIdAndStatus(idUtilisateur, Panier.StatutPanier.PANIER)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        // Ajout des produits un par un dans le panier
        for (Lister liaison : liaisonsProduits) {
            Produit produit = liaison.getProduit();
            Integer quantite = liaison.getQuantite();
            ajouterProduitAuPanierUtilisateur(idUtilisateur, produit.getId(), quantite);
        }

        // Recharger le panier pour que la liste 'lignes' soit à jour
        panier = panierRepository.findById(panier.getIdPanier())
                .orElseThrow(() -> new RuntimeException("Panier non trouvé après ajout"));

        // Mettre à jour les quantités disponibles et le statut dispo pour chaque ligne
        Integer magasinId = panier.getUtilisateur().getMagasin().getId();
        panier.getLignes().forEach(ligne -> {
            Integer stockDispo = stockerRepository.findQuantiteByProduitIdAndMagasinId(
                    ligne.getProduit().getId(), magasinId).orElse(0);
            ligne.setQuantiteDisponible(stockDispo);
            ligne.setDispo(stockDispo >= ligne.getQuantite());
        });

        return panier;
    }





    //MODIFS SO POUR DISPO
    @Transactional
    public Panier getPanierUtilisateurAvecDisponibilite(Integer userId, Integer magasinId) {
        Panier panier = panierRepository.findByUtilisateurIdAndStatus(userId, Panier.StatutPanier.PANIER)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé"));

        for (Constituer ligne : panier.getLignes()) {
            Produit produit = ligne.getProduit();
            Integer quantiteEnStock = stockerRepository.findQuantiteByProduitIdAndMagasinId(produit.getId(), magasinId)
                    .orElse(0); // Assure-toi de retourner 0 si non trouvé
            ligne.setQuantiteDisponible(quantiteEnStock);
            ligne.setDispo(quantiteEnStock >= ligne.getQuantite());
        }

        return panier;
    }


    public Panier trouverPanierActifParUtilisateur(Integer idUtilisateur) {
        return panierRepository.findByUtilisateurIdAndStatus(idUtilisateur, Panier.StatutPanier.PANIER)
                .orElse(null);
    }

    //RECUP PANIER EN COMMANDE
    public List<Panier> getCommandesParMagasin(Integer magasinId) {
        List<Panier.StatutPanier> statutsSouhaites = List.of(
                Panier.StatutPanier.COMMANDE,
                Panier.StatutPanier.EN_PREPARATION,
                Panier.StatutPanier.PRET
        );
        return panierRepository.findCommandesParMagasin(magasinId, statutsSouhaites);
    }


}