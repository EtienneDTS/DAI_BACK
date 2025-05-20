package com.pickandgo.controller;

import com.pickandgo.model.Utilisateur;
import com.pickandgo.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/login")
    public Utilisateur login(@RequestParam String email, @RequestParam String password) {
        return utilisateurService.verifierConnexion(email, password);
    }
}
