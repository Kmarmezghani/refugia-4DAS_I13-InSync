package com.awd.ms3_familles.service;

import com.awd.ms3_familles.Client.AnimalClient;
import com.awd.ms3_familles.common.exception.ConflictException;
import com.awd.ms3_familles.common.exception.ResourceNotFoundException;
import com.awd.ms3_familles.dto.PlacementRequest;
import com.awd.ms3_familles.entity.FamilleAccueil;
import com.awd.ms3_familles.entity.Placement;
import com.awd.ms3_familles.enums.IssuePlacement;
import com.awd.ms3_familles.enums.StatutPlacement;
import com.awd.ms3_familles.mapper.PlacementMapper;
import com.awd.ms3_familles.repository.FamilleRepository;
import com.awd.ms3_familles.repository.PlacementRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PlacementService {

    private final PlacementRepository placementRepository;
    private final FamilleRepository familleRepository;
    private final com.awd.ms3_familles.Client.AnimalClient animalClient;
    private final PlacementMapper mapper;

    public PlacementService(PlacementRepository placementRepository,
                            FamilleRepository familleRepository,
                            AnimalClient animalClient,
                            PlacementMapper mapper) {
        this.placementRepository = placementRepository;
        this.familleRepository = familleRepository;
        this.animalClient = animalClient;
        this.mapper = mapper;
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
            throw new ConflictException("Cet animal est déjà dans une famille d'accueil");
        }

        try {
            Map<String, Object> animal = animalClient.getAnimalById(request.animalId());

            if (animal == null || !"DISPONIBLE".equals(animal.get("statut"))) {
                throw new ConflictException("L'animal n'est pas disponible pour un placement");
            }
        } catch (feign.FeignException.NotFound e) {
            throw new ResourceNotFoundException("L'animal avec l'id " + request.animalId() + " n'existe pas dans MS1");
        } catch (feign.FeignException e) {
            throw new RuntimeException("Erreur de communication avec MS1 (Service indisponible)");
        }

        Placement placement = mapper.toEntity(request, famille);
        Placement savedPlacement = placementRepository.save(placement);

        try {
            Map<String, String> statutRequest = Map.of(
                    "nouveauStatut", "EN_ACCUEIL",
                    "motif", "Placement en famille d'accueil",
                    "origine", "MS3-Familles"
            );
            animalClient.changerStatut(request.animalId(), statutRequest);
        } catch (feign.FeignException e) {
            System.err.println("PATCH FAILED. Status: " + e.status() + " Body: " + e.contentUTF8());
        }

        return savedPlacement;
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