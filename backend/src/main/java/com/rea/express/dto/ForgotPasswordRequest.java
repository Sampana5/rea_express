package com.rea.express.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    @NotBlank(message = "L'adresse e-mail est requise")
    @Email(message = "Adresse e-mail invalide")
    private String email;

    /** Canal d'envoi du code : "email" (défaut) ou "sms" (non disponible pour l'instant). */
    private String channel;
}
