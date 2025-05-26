package com.pickandgo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MagasinDTO {
    private Integer idM;
    private String nomM;
    private String adresseM;

    // Constructeurs
    public MagasinDTO() {
    }

    public MagasinDTO(Integer idM, String nomM, String adresseM) {
        this.idM = idM;
        this.nomM = nomM;
        this.adresseM = adresseM;
    }

    // Getters et Setters
    public Integer getIdM() {
        return idM;
    }

    public void setIdM(Integer idM) {
        this.idM = idM;
    }

    public String getNomM() {
        return nomM;
    }

    public void setNomM(String nomM) {
        this.nomM = nomM;
    }

    public String getAdresseM() {
        return adresseM;
    }

    public void setAdresseM(String adresseM) {
        this.adresseM = adresseM;
    }
}
