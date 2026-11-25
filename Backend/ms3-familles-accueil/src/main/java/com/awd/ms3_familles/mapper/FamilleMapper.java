package com.awd.ms3_familles.mapper;

import com.awd.ms3_familles.dto.FamilleRequest;
import com.awd.ms3_familles.entity.FamilleAccueil;
import org.springframework.stereotype.Component;

@Component
public class FamilleMapper {

    public FamilleAccueil toEntity(FamilleRequest request) {
        if (request == null) return null;

        FamilleAccueil famille = new FamilleAccueil();
        famille.setNom(request.nom());
        famille.setTelephone(request.telephone());
        famille.setEmail(request.email());
        famille.setAdresse(request.adresse());
        famille.setCapacite(request.capacite());
        famille.setEspecesAcceptees(request.especesAcceptees());
        return famille;
    }
}