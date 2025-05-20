package com.pickandgo.controller;

import com.pickandgo.dto.ListeDeCourseDTO;
import com.pickandgo.model.ListeDeCourse;
import com.pickandgo.service.ListeDeCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listes")
public class ListeDeCourseController {

    private final ListeDeCourseService listeDeCourseService;

    @Autowired
    public ListeDeCourseController(ListeDeCourseService listeDeCourseService) {
        this.listeDeCourseService = listeDeCourseService;
    }

    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<List<ListeDeCourse>> getListesByUtilisateur(@PathVariable Integer id) {
        return ResponseEntity.ok(listeDeCourseService.getListesByUtilisateur(id));
    }

    @PostMapping
    public ResponseEntity<ListeDeCourse> createListe(@RequestBody ListeDeCourseDTO listeDeCourseDTO) {
        ListeDeCourse createdListe = listeDeCourseService.createListe(listeDeCourseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdListe);
    }
}