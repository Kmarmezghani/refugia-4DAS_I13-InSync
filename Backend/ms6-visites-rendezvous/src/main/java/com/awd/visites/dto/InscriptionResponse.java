package com.awd.visites.dto;

import com.awd.visites.entity.StatutInscription;

import java.time.LocalDateTime;

public record InscriptionResponse(
        Long id,
        Long evenementId,
        Long participantId,
        StatutInscription statut,
        LocalDateTime dateInscription
) {}