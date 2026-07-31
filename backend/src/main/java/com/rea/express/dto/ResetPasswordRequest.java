package com.rea.express.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "L'adresse e-mail est requise")
    @Email(message = "Adresse e-mail invalide")
    private String email;

    @NotBlank(message = "Le code de vérification est requis")
    private String code;

    @NotBlank(message = "Le nouveau mot de passe est requis")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String newPassword;
}
