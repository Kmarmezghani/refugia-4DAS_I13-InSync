package com.awd.animaux.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/** Format de page stable pour le frontend (évite de sérialiser PageImpl). */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> of(Page<T> p) {
        return new PageResponse<>(p.getContent(), p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
    }
}
