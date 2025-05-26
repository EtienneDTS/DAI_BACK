package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pickandgo.config.ApplicationContextProvider;
import com.pickandgo.repository.ProduitRepository;
import com.pickandgo.service.ProduitService;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "Produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idP", nullable = false)
    @JsonProperty("id")
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomP", length = 100)
    @JsonProperty("nom")
    private String nomP;

    @Column(name = "prixUnitaireP", precision = 10, scale = 2)
    @JsonProperty("prixUnitaire")
    private BigDecimal prixUnitaireP;

    @Column(name = "prixKgP", precision = 10, scale = 2)
    @JsonProperty("prixKg")
    private BigDecimal prixKgP;

    @Column(name = "poidsP")
    @JsonProperty("poids")
    private Integer poidsP;

    @Size(max = 10)
    @Column(name = "nutriP", length = 10)
    @JsonProperty("nutri")
    private String nutriP;

    @Size(max = 100)
    @Column(name = "conditionnementP", length = 100)
    @JsonProperty("conditionnement")
    private String conditionnementP;

    @Column(name = "bioP")
    @JsonProperty("bio")
    private Boolean bioP;

    @Size(max = 100)
    @Column(name = "marqueP", length = 100)
    @JsonProperty("marque")
    private String marqueP;

    @Size(max = 255)
    @Column(name = "urlImage")
    @JsonProperty("urlImage")
    private String urlImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCate")
    private Categorie idCate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idR")
    private Rayon rayon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPr")
    private Promotion promotion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Definir",
            joinColumns = @JoinColumn(name = "idP"),
            inverseJoinColumns = @JoinColumn(name = "idMc")
    )
    private List<MotCle> motsCles = new ArrayList<>();

    @OneToMany(mappedBy = "produit", fetch = FetchType.LAZY)
    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    private Set<Stocker> stockages = new HashSet<>();

    @Transient
    @JsonProperty("categorie")
    public String getNomCategorie() {
        return idCate != null ? idCate.getNomCate() : null;
    }

    @Transient
    @JsonProperty("rayon")
    public String getNomRayon() {
        return rayon != null ? rayon.getNomR() : null;
    }

    @Transient
    @JsonProperty("motsCles")
    public List<String> getListeMotsCles() {
        return motsCles.stream()
                .map(MotCle::getMotMc)
                .toList();
    }

    @Transient
    @JsonProperty("promotion")
    public Promotion getPromotion() {
        return promotion;
    }

    @Transient
    @JsonProperty("magasinsDisponibles")
    public List<Map<String, Object>> getMagasinsDisponibles() {
        if (id == null || stockages == null) {
            return new ArrayList<>();
        }

        return stockages.stream()
                .filter(stock -> stock.getQuantite() != null && stock.getQuantite() > 0)
                .map(stock -> {
                    Map<String, Object> magasinInfo = new HashMap<>();
                    Magasin magasin = stock.getMagasin();
                    magasinInfo.put("id", magasin.getId());
                    magasinInfo.put("nom", magasin.getNomM());
                    magasinInfo.put("adresse", magasin.getAdresseM());
                    magasinInfo.put("quantiteDisponible", stock.getQuantite());
                    return magasinInfo;
                })
                .collect(Collectors.toList());
    }

    @Transient
    @JsonProperty("produitsSimilaires")
    public List<Map<String, Object>> getProduitsSimilaires() {
        Integer produitId = this.id;
        List<Integer> motsClesIds = this.motsCles.stream()
                .map(MotCle::getId)
                .collect(Collectors.toList());

        ProduitRepository produitRepository = ApplicationContextProvider.getApplicationContext().getBean(ProduitRepository.class);

        // Utilisation de la méthode native optimisée
        List<Object[]> resultats = produitRepository.findSimilarProductsNative(motsClesIds, produitId);

        // Transformer les résultats en une structure simplifiée
        return resultats.stream()
                .map(row -> {
                    Produit p = (Produit) row[0]; // Adapter selon la structure renvoyée
                    Map<String, Object> produitInfo = new HashMap<>();
                    produitInfo.put("id", p.getId());
                    produitInfo.put("nom", p.getNomP());
                    produitInfo.put("prixUnitaire", p.getPrixUnitaireP());
                    produitInfo.put("urlImage", p.getUrlImage());
                    return produitInfo;
                })
                .collect(Collectors.toList());
    }
}

