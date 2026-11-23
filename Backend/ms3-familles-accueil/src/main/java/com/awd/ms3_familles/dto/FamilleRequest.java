package com.awd.ms3_familles.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record FamilleRequest(
        @NotBlank(message = "Le nom est obligatoire") String nom,
        String telephone,
        @NotBlank(message = "L'email est obligatoire") @Email(message = "L'email doit être valide") String email,
        String adresse,
        @NotNull(message = "La capacité est obligatoire") @Positive(message = "La capacité doit être supérieure à 0") Integer capacite,
        List<String> especesAcceptees
) {}