package com.awd.animaux.dto;

import com.awd.animaux.entity.Espece;
import com.awd.animaux.entity.Sexe;
import com.awd.animaux.entity.Taille;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/** Corps de POST et PUT /api/animaux. Le statut ne se change pas ici (PATCH /statut). */
public record AnimalRequest(
        @NotBlank @Size(max = 60) String nom,
        @NotNull Espece espece,
        @Size(max = 60) String race,
        @NotNull Sexe sexe,
        @PastOrPresent LocalDate dateNaissance,
        @NotNull Taille taille,
        @Size(max = 1000) String description,
        @NotNull @Min(1) @Max(5) Integer niveauEnergie,
        @NotNull Boolean ententeEnfants,
        @NotNull Boolean ententeAutresAnimaux,
        @PastOrPresent LocalDate dateArrivee
) {
}
