package com.pickandgo.controller;

import com.pickandgo.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/utilisateurs")
@CrossOrigin(origins = "http://localhost:5173")

public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @GetMapping("/ages")
    public ResponseEntity<Map<String, Long>> getStatsAge() {
        return ResponseEntity.ok(utilisateurService.getRepartitionParTrancheAge());
    }
}




