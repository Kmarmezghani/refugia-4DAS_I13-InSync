package com.awd.veterinaire.dto;

import com.awd.veterinaire.entity.StatutTraitement;
import jakarta.validation.constraints.NotNull;

public record ChangementStatutTraitementRequest(@NotNull StatutTraitement statut) {
}
