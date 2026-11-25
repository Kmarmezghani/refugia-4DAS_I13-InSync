package com.awd.visites.mapper;

import com.awd.visites.dto.VisiteRequest;
import com.awd.visites.dto.VisiteResponse;
import com.awd.visites.entity.StatutVisite;
import com.awd.visites.entity.Visite;
import org.springframework.stereotype.Component;

@Component
public class VisiteMapper {

    public Visite toEntity(VisiteRequest request) {
        return Visite.builder()
                .visiteurId(request.visiteurId())
                .animalId(request.animalId())
                .creneauId(request.creneauId())
                .nomAnimal(request.nomAnimal())
                .espece(request.espece())
                .statut(StatutVisite.CONFIRMEE)
                .build();
    }

    public VisiteResponse toResponse(Visite visite) {
        return new VisiteResponse(
                visite.getId(),
                visite.getVisiteurId(),
                visite.getAnimalId(),
                visite.getNomAnimal(),
                visite.getEspece(),
                visite.getCreneauId(),
                visite.getStatut(),
                visite.getMotifAnnulation(),
                visite.getDateCreation()
        );
    }
}