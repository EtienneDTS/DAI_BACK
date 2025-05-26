package com.pickandgo.mapper;

import com.pickandgo.dto.*;
import com.pickandgo.model.*;
import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    // Méthodes pour convertir les entités en DTOs
    public static ProduitDTO convertToDTO(Produit produit) {
        if (produit == null) {
            return null;
        }
        
        return new ProduitDTO(
            produit.getId(),  // Conversion de Integer à Long
            produit.getNom(),
            produit.getMarque(),
            produit.getPrixUnitaire(),
            produit.getPrixKg(),
            produit.getPoids(),
            produit.getConditionnement(),
            produit.getBio(),
            produit.getNutri(),
            produit.getUrlImage(),
            produit.getIdCate() != null ? produit.getIdCate().getId(): null,
            produit.getRayon() != null ? produit.getRayon().getId() : null,
            produit.getPromotion() != null ? produit.getPromotion().getId() : null
        );
    }
    
    public static MagasinDTO convertToDTO(Magasin magasin) {
        if (magasin == null) {
            return null;
        }
        
        return new MagasinDTO(
            magasin.getId(),  // Conversion de Integer à Long
            magasin.getNomM(),
            magasin.getAdresseM()
        );
    }
    
    public static UtilisateurDTO convertToDTO(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        
        return new UtilisateurDTO(
            Long.valueOf(utilisateur.getId()),  // Conversion de Integer à Long
            utilisateur.getNomU(),
            utilisateur.getPrenomU(),
            utilisateur.getEmailU(),
            utilisateur.getAdresseU(),
            utilisateur.getAgeU(),
            utilisateur.getRole(),
            convertToDTO(utilisateur.getMagasin())
        );
    }
    
    public static ConstituerDTO convertToDTO(Constituer constituer) {
        if (constituer == null) {
            return null;
        }
        
        return new ConstituerDTO(
            constituer.getId() != null ? constituer.getId().getPanierId() : null,
            convertToDTO(constituer.getProduit()),
            constituer.getQuantite()
        );
    }
    
    public static List<ConstituerDTO> convertToConstituerDTOList(List<Constituer> constituerList) {
        if (constituerList == null) {
            return null;
        }
        
        return constituerList.stream()
                .map(DTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public static ListerDTO convertToDTO(Lister lister) {
        if (lister == null) {
            return null;
        }
        
        return new ListerDTO(
            lister.getId() != null ? Long.valueOf(lister.getId().getIdL()) : null,
            convertToDTO(lister.getProduit()),
            lister.getQuantite()
        );
    }
    
    public static List<ListerDTO> convertToListerDTOList(List<Lister> listerList) {
        if (listerList == null) {
            return null;
        }
        
        return listerList.stream()
                .map(DTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public static StockerDTO convertToDTO(Stocker stocker) {
        if (stocker == null) {
            return null;
        }
        
        return new StockerDTO(
            stocker.getId() != null && stocker.getProduit() != null ? Long.valueOf(stocker.getProduit().getId()) : null,
            stocker.getId() != null && stocker.getMagasin() != null ? Long.valueOf(stocker.getMagasin().getId()) : null,
            stocker.getQuantite()
        );
    }
    
    public static List<StockerDTO> convertToStockerDTOList(List<Stocker> stockerList) {
        if (stockerList == null) {
            return null;
        }
        
        return stockerList.stream()
                .map(DTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public static PanierDTO convertToDTO(Panier panier, List<Constituer> produitsPanier) {
        if (panier == null) {
            return null;
        }
        
        return new PanierDTO(
            panier.getIdPanier(),
            convertToConstituerDTOList(produitsPanier)
        );
    }
    
    public static List<ProduitDTO> convertToProduitDTOList(List<Produit> produits) {
        if (produits == null) {
            return null;
        }
        
        return produits.stream()
                .map(DTOMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
