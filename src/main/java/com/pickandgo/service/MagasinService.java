package com.pickandgo.service;

import com.pickandgo.model.Magasin;
import com.pickandgo.repository.MagasinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MagasinService {

    @Autowired
    private MagasinRepository magasinRepository;

    public List<Magasin> getTousLesMagasins() {
        return magasinRepository.findAll();
    }
}
