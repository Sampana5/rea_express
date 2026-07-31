package com.rea.express.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubCategoryRequest {

    @NotBlank(message = "Le nom de la sous-catégorie est obligatoire")
    @Size(max = 120, message = "Le nom ne doit pas dépasser 120 caractères")
    private String name;

    @NotNull(message = "La catégorie parente (categoryId) est obligatoire")
    private Integer categoryId;

    @Size(max = 2000, message = "La description est trop longue")
    private String description;
}
