package com.pickandgo.service;

import com.pickandgo.model.*;
import com.pickandgo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ListeDeCourseService {

    private final ListeDeCourseRepository listeDeCourseRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ProduitRepository produitRepository;
    private final ListerRepository listerRepository;

    @Autowired
    public ListeDeCourseService(ListeDeCourseRepository listeDeCourseRepository,
                                UtilisateurRepository utilisateurRepository,
                                ProduitRepository produitRepository,
                                ListerRepository listerRepository) {
        this.listeDeCourseRepository = listeDeCourseRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.produitRepository = produitRepository;
        this.listerRepository = listerRepository;
    }

    @Transactional
    public List<ListeDeCourse> getListesByUtilisateur(Integer utilisateurId) {
        return listeDeCourseRepository.findByUtilisateurId(utilisateurId);
    }

    @Transactional
    public ListeDeCourse createListe(String nom, Integer idUtilisateur) {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        ListeDeCourse listeDeCourse = new ListeDeCourse();
        listeDeCourse.setNoml(nom);
        listeDeCourse.setUtilisateur(utilisateur);

        return listeDeCourseRepository.save(listeDeCourse);
    }

    @Transactional
    public void ajouterProduitDansListe(Integer idListe, Integer idProduit, Integer quantite) {
        ListeDeCourse liste = listeDeCourseRepository.findById(idListe)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        Produit produit = produitRepository.findById(idProduit)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        Optional<Lister> liaisonExistanteOpt = listerRepository.findByListeIdAndProduitId(idListe, idProduit);

        if (liaisonExistanteOpt.isPresent()) {
            // Le produit est déjà dans la liste => on augmente la quantité
            Lister liaisonExistante = liaisonExistanteOpt.get();
            liaisonExistante.setQuantite(liaisonExistante.getQuantite() + quantite);
            listerRepository.save(liaisonExistante);
        } else {
            // Nouveau lien entre produit et liste
            Lister nouvelleLiaison = new Lister();
            ListerId id = new ListerId();
            id.setIdL(idListe);
            id.setIdP(idProduit);
            nouvelleLiaison.setId(id);
            nouvelleLiaison.setListe(liste);
            nouvelleLiaison.setProduit(produit);
            nouvelleLiaison.setQuantite(quantite);
            listerRepository.save(nouvelleLiaison);
        }
    }



    @Transactional
    public boolean mettreAJourQuantite(Integer idListe, Integer idProduit, int nouvelleQuantite) {
        Optional<Lister> listerOpt = listerRepository.findById(new ListerId(idListe, idProduit));

        if (listerOpt.isPresent()) {
            if (nouvelleQuantite == 0) {
                listerRepository.delete(listerOpt.get());
            } else {
                Lister lister = listerOpt.get();
                lister.setQuantite(nouvelleQuantite);
                listerRepository.save(lister);
            }
            return true;
        }
        return false;
    }

    @Transactional
    public boolean supprimerListeParId(Integer idListe) {
        Optional<ListeDeCourse> listeOpt = listeDeCourseRepository.findById(idListe);

        if (listeOpt.isPresent()) {
            listeDeCourseRepository.deleteById(idListe);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Optional<ListeDeCourse> getListeParId(Integer idListe) {
        return listeDeCourseRepository.findById(idListe);
    }


    @Autowired
    private PostItRepository postItRepository;

    @Transactional
    public PostIt ajouterPostItVide(Integer idListe) {
        ListeDeCourse liste = listeDeCourseRepository.findById(idListe)
                .orElseThrow(() -> new RuntimeException("Liste non trouvée"));

        PostIt postIt = new PostIt();
        postIt.setTexte("");
        postIt.setListe(liste);

        return postItRepository.save(postIt);
    }

    @Transactional
    public PostIt modifierPostIt(Integer idPostIt, String nouveauTexte) {
        PostIt postIt = postItRepository.findById(idPostIt)
                .orElseThrow(() -> new RuntimeException("Post-it non trouvé"));

        postIt.setTexte(nouveauTexte);
        return postItRepository.save(postIt);
    }

    @Transactional
    public void supprimerPostIt(Integer idPostIt) {
        if (!postItRepository.existsById(idPostIt)) {
            throw new RuntimeException("Post-it non trouvé");
        }

        postItRepository.deleteById(idPostIt);
    }

    @Transactional
    public boolean supprimerProduitDeListe(Integer idListe, Integer idProduit) {
        Optional<Lister> liaison = listerRepository.findByListeIdAndProduitId(idListe, idProduit);

        if (liaison.isPresent()) {
            listerRepository.delete(liaison.get());
            return true;
        } else {
            return false;
        }
    }




}
