package com.rea.express.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 160, message = "Le nom ne doit pas dépasser 160 caractères")
    private String name;

    @Size(max = 4000, message = "La description est trop longue")
    private String description;

    @Size(max = 80, message = "La référence est trop longue")
    private String reference;

    /** Chemin relatif de l'image principale (jamais de binaire base64). */
    @Size(max = 500, message = "Le chemin de l'image est trop long")
    private String imageUrl;

    @Size(max = 4000, message = "Les informations techniques sont trop longues")
    private String technicalInfo;

    @Size(max = 120, message = "La marque est trop longue")
    private String brand;

    @Size(max = 120, message = "La référence fabricant est trop longue")
    private String referenceManufacturer;

    @Size(max = 60, message = "L'unité de vente est trop longue")
    private String unitOfSale;

    @Size(max = 60, message = "La disponibilité est trop longue")
    private String availability;

    @NotNull(message = "La sous-catégorie (subCategoryId) est obligatoire")
    private Integer subCategoryId;
}
