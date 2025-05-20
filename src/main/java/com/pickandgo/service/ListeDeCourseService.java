package com.pickandgo.service;

import com.pickandgo.dto.ListeDeCourseDTO;
import com.pickandgo.model.ListeDeCourse;
import com.pickandgo.model.Utilisateur;
import com.pickandgo.repository.ListeDeCourseRepository;
import com.pickandgo.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListeDeCourseService {

    private final ListeDeCourseRepository listeDeCourseRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public ListeDeCourseService(ListeDeCourseRepository listeDeCourseRepository,
                                UtilisateurRepository utilisateurRepository) {
        this.listeDeCourseRepository = listeDeCourseRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public List<ListeDeCourse> getListesByUtilisateur(Integer utilisateurId) {
        return listeDeCourseRepository.findByUtilisateurId(utilisateurId);
    }

    public ListeDeCourse createListe(ListeDeCourseDTO listeDeCourseDTO) {
        Utilisateur utilisateur = utilisateurRepository.findById(listeDeCourseDTO.getIdUtilisateur())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        ListeDeCourse listeDeCourse = new ListeDeCourse();
        listeDeCourse.setNoml(listeDeCourseDTO.getNom());
        listeDeCourse.setUtilisateur(utilisateur);

        return listeDeCourseRepository.save(listeDeCourse);
    }
}