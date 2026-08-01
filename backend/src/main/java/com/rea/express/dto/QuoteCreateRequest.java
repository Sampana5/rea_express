package com.rea.express.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuoteCreateRequest {

    @Size(max = 2000, message = "Le message ne peut pas dépasser 2000 caractères.")
    private String message;
}
