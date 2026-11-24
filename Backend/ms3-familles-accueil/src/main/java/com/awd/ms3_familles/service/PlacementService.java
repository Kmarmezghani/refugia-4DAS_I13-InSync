package com.awd.ms3_familles.service;

import com.awd.ms3_familles.common.exception.ConflictException;
import com.awd.ms3_familles.common.exception.ResourceNotFoundException;
import com.awd.ms3_familles.dto.PlacementRequest;
import com.awd.ms3_familles.entity.FamilleAccueil;
import com.awd.ms3_familles.entity.Placement;
import com.awd.ms3_familles.enums.IssuePlacement;
import com.awd.ms3_familles.enums.StatutPlacement;
import com.awd.ms3_familles.repository.FamilleRepository;
import com.awd.ms3_familles.repository.PlacementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PlacementService {

    private final PlacementRepository placementRepository;
    private final FamilleRepository familleRepository;

    public PlacementService(PlacementRepository placementRepository, FamilleRepository familleRepository) {
        this.placementRepository = placementRepository;
        this.familleRepository = familleRepository;
    }

    public Placement creerPlacement(PlacementRequest request) {
        FamilleAccueil famille = familleRepository.findById(request.familleId())
                .orElseThrow(() -> new ResourceNotFoundException("Famille introuvable"));

        List<Placement> placementsActifs = placementRepository.findByFamilleId(famille.getId())
                .stream()
                .filter(p -> p.getStatut() == StatutPlacement.EN_COURS)
                .toList();

        if (placementsActifs.size() >= famille.getCapacite()) {
            throw new ConflictException("La famille est pleine");
        }

        boolean animalDejaPlace = placementRepository
                .findByAnimalIdAndStatut(request.animalId(), StatutPlacement.EN_COURS)
                .isPresent();

        if (animalDejaPlace) {
            throw new ConflictException("Cet animal est déjà dans une famille d'accueil");        }

        Placement placement = new Placement();
        placement.setAnimalId(request.animalId());
        placement.setFamille(famille);
        placement.setDateDebut(request.dateDebut());
        placement.setDateFinPrevue(request.dateFinPrevue());
        placement.setNotes(request.notes());

        return placementRepository.save(placement);
    }
    public List<Placement> getPlacements(StatutPlacement statut, Long familleId, Long animalId) {
        if (statut != null) {
            return placementRepository.findByStatut(statut);
        }
        if (familleId != null) {
            return placementRepository.findByFamilleId(familleId);
        }
        if (animalId != null) {
            return placementRepository.findByAnimalId(animalId);
        }
        return placementRepository.findAll();
    }

    public Placement getPlacementById(Long id) {
        return placementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Placement introuvable avec l'id : " + id));
    }
    public Placement terminerPlacement(Long id, IssuePlacement issue) {
        Placement placement = getPlacementById(id);

        if (placement.getStatut() != StatutPlacement.EN_COURS) {
            throw new ConflictException("Seul un placement EN_COURS peut être terminé");
        }

        placement.setStatut(StatutPlacement.TERMINE);
        placement.setDateFinReelle(LocalDate.now());
        placement.setIssue(issue);
        return placementRepository.save(placement);
    }

    public Placement annulerPlacement(Long id) {
        Placement placement = getPlacementById(id);

        if (placement.getStatut() != StatutPlacement.EN_COURS) {
            throw new ConflictException("Seul un placement EN_COURS peut être annulé");
        }

        placement.setStatut(StatutPlacement.ANNULE);
        placement.setDateFinReelle(LocalDate.now());
        placement.setIssue(IssuePlacement.ANNULATION);
        return placementRepository.save(placement);
    }
}