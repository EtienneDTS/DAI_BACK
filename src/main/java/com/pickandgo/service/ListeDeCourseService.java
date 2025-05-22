package com.pickandgo.service;

import com.pickandgo.model.*;
import com.pickandgo.repository.ListeDeCourseRepository;
import com.pickandgo.repository.ListerRepository;
import com.pickandgo.repository.ProduitRepository;
import com.pickandgo.repository.UtilisateurRepository;
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

        ListerId listerId = new ListerId();
        listerId.setIdL(idListe);
        listerId.setIdP(idProduit);

        Lister lister = new Lister();
        lister.setId(listerId);
        lister.setListe(liste);
        lister.setProduit(produit);
        lister.setQuantite(quantite);

        listerRepository.save(lister);
    }

    @Transactional
    public void supprimerProduitDeListe(Integer idListe, Integer idProduit) {
        listerRepository.deleteByIdIdLAndIdIdP(idListe, idProduit);
    }

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

    public boolean supprimerListeParId(Integer idListe) {
        Optional<ListeDeCourse> listeOpt = listeDeCourseRepository.findById(idListe);

        if (listeOpt.isPresent()) {
            listeDeCourseRepository.deleteById(idListe);
            return true;
        } else {
            return false;
        }
    }


}
