package com.awd.visites.mapper;

import com.awd.visites.dto.InscriptionRequest;
import com.awd.visites.dto.InscriptionResponse;
import com.awd.visites.entity.Inscription;
import com.awd.visites.entity.StatutInscription;
import org.springframework.stereotype.Component;

@Component
public class InscriptionMapper {

    public Inscription toEntity(Long evenementId, InscriptionRequest request) {
        return Inscription.builder()
                .evenementId(evenementId)
                .participantId(request.participantId())
                .statut(StatutInscription.INSCRIT)
                .build();
    }

    public InscriptionResponse toResponse(Inscription inscription) {
        return new InscriptionResponse(
                inscription.getId(),
                inscription.getEvenementId(),
                inscription.getParticipantId(),
                inscription.getStatut(),
                inscription.getDateInscription()
        );
    }
}