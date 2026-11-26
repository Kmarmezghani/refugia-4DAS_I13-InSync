package com.awd.adoptions.dto;

import com.awd.adoptions.entity.TypeLogement;

public record ProfilResponse(
        Long id, String clientId, TypeLogement typeLogement, boolean jardin,
        int nbPersonnes, boolean enfants, boolean autresAnimaux,
        String experienceAnimaux, String disponibilites) {
}