package com.awd.visites.dto;

import com.awd.visites.entity.StatutEvenement;
import com.awd.visites.entity.TypeEvenement;

import java.time.LocalDateTime;

public record EvenementResponse(
        Long id,
        String titre,
        TypeEvenement type,
        String description,
        LocalDateTime date,
        String lieu,
        int capacite,
        int placesRestantes,
        StatutEvenement statut
) {}