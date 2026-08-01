package com.rea.express.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequest {

    /** Requis pour l'ajout ; ignoré / redondant pour la mise à jour (path). */
    private Integer productId;

    @NotNull(message = "La quantité est requise.")
    @Min(value = 1, message = "La quantité minimale est 1.")
    @Max(value = 9999, message = "La quantité maximale est 9999.")
    private Integer quantity = 1;
}
