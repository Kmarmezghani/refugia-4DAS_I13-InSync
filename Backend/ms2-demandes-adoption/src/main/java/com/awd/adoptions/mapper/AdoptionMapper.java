package com.awd.adoptions.mapper;

import com.awd.adoptions.dto.*;
import com.awd.adoptions.entity.DemandeAdoption;
import com.awd.adoptions.entity.ProfilCandidat;
import org.springframework.stereotype.Component;

@Component
public class AdoptionMapper {

    public ProfilCandidat toEntity(ProfilRequest r) {
        ProfilCandidat p = new ProfilCandidat();
        p.setClientId(r.clientId());
        p.setTypeLogement(r.typeLogement());
        p.setJardin(r.jardin());
        p.setNbPersonnes(r.nbPersonnes());
        p.setEnfants(r.enfants());
        p.setAutresAnimaux(r.autresAnimaux());
        p.setExperienceAnimaux(r.experienceAnimaux());
        p.setDisponibilites(r.disponibilites());
        return p;
    }

    public void update(ProfilCandidat p, ProfilUpdateRequest r) {
        p.setTypeLogement(r.typeLogement());
        p.setJardin(r.jardin());
        p.setNbPersonnes(r.nbPersonnes());
        p.setEnfants(r.enfants());
        p.setAutresAnimaux(r.autresAnimaux());
        p.setExperienceAnimaux(r.experienceAnimaux());
        p.setDisponibilites(r.disponibilites());
    }

    public ProfilResponse toResponse(ProfilCandidat p) {
        return new ProfilResponse(p.getId(), p.getClientId(), p.getTypeLogement(),
                p.isJardin(), p.getNbPersonnes(), p.isEnfants(), p.isAutresAnimaux(),
                p.getExperienceAnimaux(), p.getDisponibilites());
    }

    public DemandeResponse toResponse(DemandeAdoption d) {
        return new DemandeResponse(d.getId(), d.getAnimalId(), d.getClientId(),
                d.getStatut(), d.getMotifRefus(), d.getDateCreation(), d.getDateDecision(),
                d.getScore() != null ? d.getScore().getScoreTotal() : null);
    }
}