package com.pickandgo.dto;

public class UtilisateurDTO {
    private Long idU;
    private String nomU;
    private String prenomU;
    private String emailU;
    private String adresseU;
    private Integer ageU;
    private String role;
    private MagasinDTO magasin;

    // Constructeurs
    public UtilisateurDTO() {
    }

    public UtilisateurDTO(Long idU, String nomU, String prenomU, String emailU, 
                         String adresseU, Integer ageU, String role, MagasinDTO magasin) {
        this.idU = idU;
        this.nomU = nomU;
        this.prenomU = prenomU;
        this.emailU = emailU;
        this.adresseU = adresseU;
        this.ageU = ageU;
        this.role = role;
        this.magasin = magasin;
    }

    // Getters et Setters
    public Long getIdU() {
        return idU;
    }

    public void setIdU(Long idU) {
        this.idU = idU;
    }

    public String getNomU() {
        return nomU;
    }

    public void setNomU(String nomU) {
        this.nomU = nomU;
    }

    public String getPrenomU() {
        return prenomU;
    }

    public void setPrenomU(String prenomU) {
        this.prenomU = prenomU;
    }

    public String getEmailU() {
        return emailU;
    }

    public void setEmailU(String emailU) {
        this.emailU = emailU;
    }

    public String getAdresseU() {
        return adresseU;
    }

    public void setAdresseU(String adresseU) {
        this.adresseU = adresseU;
    }

    public Integer getAgeU() {
        return ageU;
    }

    public void setAgeU(Integer ageU) {
        this.ageU = ageU;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public MagasinDTO getMagasin() {
        return magasin;
    }

    public void setMagasin(MagasinDTO magasin) {
        this.magasin = magasin;
    }
}
