package com.pickandgo.service;

import com.pickandgo.model.Promotion;
import com.pickandgo.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }
}
