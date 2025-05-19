package com.pickandgo.model;

public class Utilisateur {
    private final int idU;
    private String prenomU;
    private String nomU;
    private String emailU;
    private Role role;
    private String password;
    private String adresseU;

    public Utilisateur(int idU, String prenomU, String nomU, String emailU, Role role, String password, String adresseU) {
        this.idU = idU;
        this.prenomU = prenomU;
        this.nomU = nomU;
        this.emailU = emailU;
        this.role = role;
        this.password = password;
        this.adresseU = adresseU;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idU=" + idU +
                ", prenomU='" + prenomU + '\'' +
                ", nomU='" + nomU + '\'' +
                ", emailU='" + emailU + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                ", adresseU='" + adresseU + '\'' +
                '}';
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
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public void setAdresseU(String adresseU) {
        this.adresseU = adresseU;
    }

    public String getAdresseU() {
        return adresseU;
    }

    public int getIdU() {
        return idU;
    }

    //A verifier
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
