package com.pickandgo.mapper;

import com.pickandgo.dto.*;
import com.pickandgo.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    public static ProduitDTO convertToDTO(Produit produit) {
        if (produit == null) {
            return null;
        }

        ProduitDTO dto = new ProduitDTO(
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

        // Ajout des mots-clés
        if (produit.getMotsCles() != null) {
            List<String> motsClesTexte = produit.getMotsCles().stream()
                    .map(MotCle::getMotMc)  // On suppose que MotCle a un attribut 'libelle'
                    .collect(Collectors.toList());
            dto.setMotsCles(motsClesTexte);
        }

        return dto;
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

    public static PromotionDTO convertToDTO(Promotion promotion) {
        if (promotion == null) {
            return null;
        }

        return new PromotionDTO(
                promotion.getId(),
                promotion.getNomPr(),
                promotion.getUrlImagePromo(),
                promotion.getTypePr()
        );
    }

    public static List<MagasinStockDTO> convertToMagasinStockDTOList(List<Stocker> stockages) {
        if (stockages == null || stockages.isEmpty()) {
            return new ArrayList<>();
        }

        return stockages.stream()
                .filter(s -> s.getMagasin() != null && s.getQuantite() > 0)
                .map(s -> new MagasinStockDTO(
                        s.getMagasin().getId(),
                        s.getMagasin().getNomM(),
                        s.getMagasin().getAdresseM(),
                        s.getQuantite()
                ))
                .collect(Collectors.toList());
    }

    public static ProduitDTO convertToDTO(Produit produit, List<Produit> similaires) {
        if (produit == null) {
            return null;
        }

        ProduitDTO dto = new ProduitDTO(
                produit.getId(),
                produit.getNom(),
                produit.getMarque(),
                produit.getPrixUnitaire(),
                produit.getPrixKg(),
                produit.getPoids(),
                produit.getConditionnement(),
                produit.getBio(),
                produit.getNutri(),
                produit.getUrlImage(),
                produit.getIdCate() != null ? produit.getIdCate().getId() : null,
                produit.getRayon() != null ? produit.getRayon().getId() : null,
                produit.getPromotion() != null ? produit.getPromotion().getId() : null,
                produit.getNomCategorie(),
                produit.getNomRayon()
        );

        // Ajout des mots-clés
        if (produit.getMotsCles() != null) {
            List<String> motsClesTexte = produit.getMotsCles().stream()
                    .map(MotCle::getMotMc)
                    .collect(Collectors.toList());
            dto.setMotsCles(motsClesTexte);
        }

        // Ajouter les informations sur la promotion
        if (produit.getPromotion() != null) {
            dto.setPromotion(convertToDTO(produit.getPromotion()));
        }

        // Ajouter les informations sur les disponibilités en magasin
        if (produit.getStockages() != null) {
            dto.setDisponibilites(convertToMagasinStockDTOList(produit.getStockages()));
        }

        // Conversion simplifiée des produits similaires pour éviter la récursivité
        if (similaires != null && !similaires.isEmpty()) {
            List<ProduitDTO> similairesDTOs = similaires.stream()
                    .map(similaire -> {
                        ProduitDTO similaireDTO = new ProduitDTO(
                                similaire.getId(),
                                similaire.getNom(),
                                similaire.getMarque(),
                                similaire.getPrixUnitaire(),
                                similaire.getPrixKg(),
                                similaire.getPoids(),
                                similaire.getConditionnement(),
                                similaire.getBio(),
                                similaire.getNutri(),
                                similaire.getUrlImage(),
                                similaire.getIdCate() != null ? similaire.getIdCate().getId() : null,
                                similaire.getRayon() != null ? similaire.getRayon().getId() : null,
                                similaire.getPromotion() != null ? similaire.getPromotion().getId() : null,
                                similaire.getNomCategorie(),
                                similaire.getNomRayon()
                        );

                        // Ajouter uniquement les mots-clés aux produits similaires
                        if (similaire.getMotsCles() != null) {
                            similaireDTO.setMotsCles(similaire.getMotsCles().stream()
                                    .map(MotCle::getMotMc)
                                    .collect(Collectors.toList()));
                        }

                        return similaireDTO;
                    })
                    .collect(Collectors.toList());

            dto.setProduitsSimilaires(similairesDTOs);
        }

        return dto;
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
