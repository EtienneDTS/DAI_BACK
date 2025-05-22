package com.pickandgo.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class ListerId implements Serializable {
    //Permet de definir une clé primaire composite
    private Integer idL;
    private Integer idP;
}
