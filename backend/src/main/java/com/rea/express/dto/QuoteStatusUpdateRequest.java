package com.rea.express.dto;

import com.rea.express.POJO.QuoteStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuoteStatusUpdateRequest {

    @NotNull(message = "Le statut est requis.")
    private QuoteStatus status;

    @Size(max = 2000, message = "Les notes admin ne peuvent pas dépasser 2000 caractères.")
    private String adminNotes;

    private BigDecimal quotedAmount;
}
