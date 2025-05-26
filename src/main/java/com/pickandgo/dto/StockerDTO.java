package com.pickandgo.dto;

public class StockerDTO {
    private Long idP;
    private Long idM;
    private Integer quantiteS;

    // Constructeurs
    public StockerDTO() {
    }

    public StockerDTO(Long idP, Long idM, Integer quantiteS) {
        this.idP = idP;
        this.idM = idM;
        this.quantiteS = quantiteS;
    }

    // Getters et Setters
    public Long getIdP() {
        return idP;
    }

    public void setIdP(Long idP) {
        this.idP = idP;
    }

    public Long getIdM() {
        return idM;
    }

    public void setIdM(Long idM) {
        this.idM = idM;
    }

    public Integer getQuantiteS() {
        return quantiteS;
    }

    public void setQuantiteS(Integer quantiteS) {
        this.quantiteS = quantiteS;
    }
}
