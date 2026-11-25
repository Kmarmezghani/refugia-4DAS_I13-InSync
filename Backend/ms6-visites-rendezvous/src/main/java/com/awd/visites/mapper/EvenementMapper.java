package com.awd.visites.mapper;

import com.awd.visites.dto.EvenementRequest;
import com.awd.visites.dto.EvenementResponse;
import com.awd.visites.entity.Evenement;
import com.awd.visites.entity.StatutEvenement;
import org.springframework.stereotype.Component;

@Component
public class EvenementMapper {

    public Evenement toEntity(EvenementRequest request) {
        return Evenement.builder()
                .titre(request.titre())
                .type(request.type())
                .description(request.description())
                .date(request.date())
                .lieu(request.lieu())
                .capacite(request.capacite())
                .placesRestantes(request.capacite())
                .statut(StatutEvenement.PLANIFIE)
                .build();
    }

    public EvenementResponse toResponse(Evenement evenement) {
        return new EvenementResponse(
                evenement.getId(),
                evenement.getTitre(),
                evenement.getType(),
                evenement.getDescription(),
                evenement.getDate(),
                evenement.getLieu(),
                evenement.getCapacite(),
                evenement.getPlacesRestantes(),
                evenement.getStatut()
        );
    }
}