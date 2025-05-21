package com.pickandgo.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NouveauProduitDTO {
    private String nomP;
    private BigDecimal prixUnitaireP;
    private BigDecimal prixKgP;
    private Integer poidsP;
    private String nutriP;
    private String conditionnementP;
    private Boolean bioP;
    private String marqueP;
    private String urlImage;
    private Integer categorieId;
}