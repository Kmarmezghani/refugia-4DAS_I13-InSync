package com.refugia.ms3_familles.service;

import com.refugia.ms3_familles.dto.FamilleRequest;
import com.refugia.ms3_familles.entity.FamilleAccueil;
import com.refugia.ms3_familles.repository.FamilleRepository;
import org.springframework.stereotype.Service;

@Service
public class FamilleService {

    private final FamilleRepository repository;

    public FamilleService(FamilleRepository repository) {
        this.repository = repository;
    }

    public FamilleAccueil creerFamille(FamilleRequest request) {
        FamilleAccueil famille = new FamilleAccueil();
        famille.setNom(request.nom());
        famille.setTelephone(request.telephone());
        famille.setEmail(request.email());
        famille.setAdresse(request.adresse());
        famille.setCapacite(request.capacite());
        famille.setEspecesAcceptees(request.especesAcceptees());
        return repository.save(famille);
    }
}