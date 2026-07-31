package com.rea.express.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GithubLoginRequest {

    @NotBlank(message = "Le code GitHub est requis")
    private String code;
}
