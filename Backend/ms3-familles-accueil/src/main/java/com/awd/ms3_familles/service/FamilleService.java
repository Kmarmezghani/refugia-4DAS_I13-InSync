package com.awd.ms3_familles.service;

import com.awd.ms3_familles.common.exception.ResourceNotFoundException;
import com.awd.ms3_familles.dto.FamilleRequest;
import com.awd.ms3_familles.entity.FamilleAccueil;
import com.awd.ms3_familles.enums.StatutFamille;
import com.awd.ms3_familles.repository.FamilleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<FamilleAccueil> getFamilles(StatutFamille statut, String espece) {
        if (statut != null) {
            return repository.findByStatut(statut);
        }
        if (espece != null && !espece.isEmpty()) {
            return repository.findByEspece(espece);
        }
        return repository.findAll();
    }

    public FamilleAccueil getFamilleById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Famille introuvable avec l'id : " + id));
    }
}