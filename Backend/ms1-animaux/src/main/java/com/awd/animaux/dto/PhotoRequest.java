package com.awd.animaux.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Ajout d'une photo par son URL ; la première photo devient automatiquement la principale. */
public record PhotoRequest(
        @NotBlank @Size(max = 500) String url,
        boolean principale
) {
}
