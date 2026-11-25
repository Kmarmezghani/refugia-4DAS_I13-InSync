package com.awd.visites.service;

import com.awd.visites.common.exception.ConflictException;
import com.awd.visites.common.exception.ResourceNotFoundException;
import com.awd.visites.dto.VisiteRequest;
import com.awd.visites.dto.VisiteResponse;
import com.awd.visites.entity.StatutVisite;
import com.awd.visites.entity.Visite;
import com.awd.visites.mapper.VisiteMapper;
import com.awd.visites.repository.CreneauRepository;
import com.awd.visites.repository.VisiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisiteService {

    private final VisiteRepository visiteRepository;
    private final CreneauRepository creneauRepository;
    private final VisiteMapper mapper;

    @Transactional
    public VisiteResponse creer(VisiteRequest request) {
        if (!creneauRepository.existsById(request.creneauId())) {
            throw new ResourceNotFoundException("Créneau introuvable : " + request.creneauId());
        }
        // TODO (plus tard) : vérifier l'animal auprès de MS1 (Feign), réserver une place
        // du créneau (409 si complet), publier l'événement VisiteCréée.
        Visite visite = visiteRepository.save(mapper.toEntity(request));
        return mapper.toResponse(visite);
    }

    @Transactional(readOnly = true)
    public List<VisiteResponse> lister(Long visiteurId, Long animalId) {
        List<Visite> visites;
        if (visiteurId != null && animalId != null) {
            visites = visiteRepository.findByVisiteurIdAndAnimalId(visiteurId, animalId);
        } else if (visiteurId != null) {
            visites = visiteRepository.findByVisiteurId(visiteurId);
        } else if (animalId != null) {
            visites = visiteRepository.findByAnimalId(animalId);
        } else {
            visites = visiteRepository.findAll();
        }
        return visites.stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public void annuler(Long id, String motif) {
        Visite visite = trouver(id);
        if (visite.getStatut() != StatutVisite.CONFIRMEE) {
            throw new ConflictException("Seule une visite confirmée peut être annulée");
        }
        // TODO (plus tard) : règle des 24 h, libérer la place, événement VisiteAnnulée.
        visite.setStatut(StatutVisite.ANNULEE);
        visite.setMotifAnnulation(motif);
        visiteRepository.save(visite);
    }

    @Transactional
    public VisiteResponse marquerEffectuee(Long id) {
        Visite visite = trouver(id);
        if (visite.getStatut() != StatutVisite.CONFIRMEE) {
            throw new ConflictException("Seule une visite confirmée peut être marquée comme effectuée");
        }
        visite.setStatut(StatutVisite.EFFECTUEE);
        return mapper.toResponse(visiteRepository.save(visite));
    }

    private Visite trouver(Long id) {
        return visiteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visite introuvable : " + id));
    }
}