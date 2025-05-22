package com.pickandgo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPr", nullable = false)
    @JsonProperty("idPromotion")
    private Integer id;

    @Size(max = 100)
    @Column(name = "nomPr", length = 100)
    @JsonProperty("nomPromotion")
    private String nomPr;

    @Size(max = 255)
    @Column(name = "urlImagePromo")
    @JsonProperty("urlImagePromotion")
    private String urlImagePromo;

    @Size(max = 255)
    @Column(name = "typePr")
    @JsonProperty("typePromotion")
    private String typePr;

}