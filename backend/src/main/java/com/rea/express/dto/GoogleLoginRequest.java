package com.rea.express.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequest {

    @NotBlank(message = "Le jeton Google est requis")
    private String idToken;
}
