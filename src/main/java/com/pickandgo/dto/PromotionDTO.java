package com.pickandgo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PromotionDTO {
    private Integer id;
    private String nomPr;
    private String urlImagePromo;
    private String typePr;

    public PromotionDTO() {}

    public PromotionDTO(Integer id, String nomPr, String urlImagePromo, String typePr) {
        this.id = id;
        this.nomPr = nomPr;
        this.urlImagePromo = urlImagePromo;
        this.typePr = typePr;
    }

    // Getters et setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getNomPr() {
        return nomPr;
    }
    public void setNomPr(String nomPr) {
        this.nomPr = nomPr;
    }
    public String getUrlImagePromo() {
        return urlImagePromo;
    }
    public void setUrlImagePromo(String urlImagePromo) {
        this.urlImagePromo = urlImagePromo;
    }
    public String getTypePr() {
        return typePr;
    }
    public void setTypePr(String typePr) {
        this.typePr = typePr;
    }
}