package com.awd.dons.common.exception;

import java.time.LocalDateTime;

/** Corps d'erreur commun à tous les services (section 8.2 du dossier). */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
