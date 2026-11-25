package com.awd.dons.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DonateurRequest(

        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 100, message = "Le nom fait au maximum 100 caractères")
        String nom,

        @NotBlank(message = "L'e-mail est obligatoire")
        @Email(message = "E-mail invalide")
        String email,

        @Size(max = 20, message = "Le téléphone fait au maximum 20 caractères")
        String telephone
) {
}
