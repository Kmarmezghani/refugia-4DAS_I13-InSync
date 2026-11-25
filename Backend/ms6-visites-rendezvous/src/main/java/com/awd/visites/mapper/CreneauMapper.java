package com.awd.visites.mapper;

import com.awd.visites.dto.CreneauRequest;
import com.awd.visites.dto.CreneauResponse;
import com.awd.visites.entity.Creneau;
import org.springframework.stereotype.Component;

@Component
public class CreneauMapper {

    // DTO reçu du client -> entité à enregistrer en base
    public Creneau toEntity(CreneauRequest r) {
        return Creneau.builder()
                .date(r.date())
                .heureDebut(r.heureDebut())
                .heureFin(r.heureFin())
                .capacite(r.capacite())
                .placesRestantes(r.capacite())
                .build();
    }

    // entité de la base -> DTO renvoyé au client
    public CreneauResponse toResponse(Creneau c) {
        return new CreneauResponse(
                c.getId(),
                c.getDate(),
                c.getHeureDebut(),
                c.getHeureFin(),
                c.getCapacite(),
                c.getPlacesRestantes());
    }
}