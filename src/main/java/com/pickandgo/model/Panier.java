package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Panier")
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPa", nullable = false)
    @JsonProperty("idPanier")
    private Integer idPanier;

    @Column(name = "prixtotalPa", precision = 10, scale = 2)
    private BigDecimal prixtotalPa = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private StatutPanier status = StatutPanier.PANIER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idU")
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Constituer> lignes = new ArrayList<>();

    public enum StatutPanier {
        PANIER,
        COMMANDE,
        EN_PREPARATION,
        PRET,
        RECUPERE
    }

    @OneToMany(mappedBy = "idPa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("panier-commande")
    @JsonIgnore
    private List<Commander> commandes = new ArrayList<>();


    //Test pour nom créneau
    @Transient
    @JsonProperty("dateC")
    private LocalDate dateC;

    @Transient
    @JsonProperty("creneauChoisi")
    private String creneauChoisi;

    @Transient
    @JsonProperty("magasin")
    private Magasin magasin;
}