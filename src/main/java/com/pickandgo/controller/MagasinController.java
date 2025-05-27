package com.pickandgo.controller;

import com.pickandgo.model.Magasin;
import com.pickandgo.service.MagasinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/magasins")
@CrossOrigin(origins = "http://localhost:5173")
public class MagasinController {

    @Autowired
    private MagasinService magasinService;

    @GetMapping
    public ResponseEntity<List<Magasin>> getMagasins() {
        List<Magasin> magasins = magasinService.getTousLesMagasins();
        return ResponseEntity.ok(magasins);
    }
}
