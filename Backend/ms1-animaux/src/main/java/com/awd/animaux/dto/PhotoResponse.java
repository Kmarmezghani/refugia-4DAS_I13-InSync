package com.awd.animaux.dto;

import java.time.LocalDateTime;

public record PhotoResponse(Long id, String url, boolean principale, LocalDateTime dateAjout) {
}
