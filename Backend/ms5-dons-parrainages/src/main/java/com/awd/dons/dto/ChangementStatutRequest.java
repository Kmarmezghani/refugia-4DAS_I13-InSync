package com.awd.dons.dto;

import com.awd.dons.entity.StatutParrainage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangementStatutRequest(
        @NotNull(message = "Le nouveau statut est obligatoire")
        StatutParrainage statut,

        @Size(max = 255)
        String motif
) {
}
