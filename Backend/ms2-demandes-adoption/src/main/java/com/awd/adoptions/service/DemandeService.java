package com.awd.adoptions.service;

import com.awd.adoptions.common.exception.BadRequestException;
import com.awd.adoptions.common.exception.ConflictException;
import com.awd.adoptions.common.exception.ResourceNotFoundException;
import com.awd.adoptions.dto.DemandeRequest;
import com.awd.adoptions.dto.DemandeResponse;
import com.awd.adoptions.dto.StatutRequest;
import com.awd.adoptions.entity.DemandeAdoption;
import com.awd.adoptions.entity.StatutDemande;
import com.awd.adoptions.mapper.AdoptionMapper;
import com.awd.adoptions.repository.DemandeAdoptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.awd.adoptions.entity.StatutDemande.*;

@Service
@RequiredArgsConstructor
@Transactional
public class DemandeService {

    private static final Map<StatutDemande, Set<StatutDemande>> TRANSITIONS = Map.of(
            EN_ATTENTE, EnumSet.of(EN_ETUDE, ANNULEE),
            EN_ETUDE, EnumSet.of(ACCEPTEE, REFUSEE, ANNULEE),
            ACCEPTEE, EnumSet.of(FINALISEE, ANNULEE));

    private static final Set<StatutDemande> STATUTS_ACTIFS = EnumSet.of(EN_ATTENTE, EN_ETUDE, ACCEPTEE);

    private final DemandeAdoptionRepository repository;
    private final AdoptionMapper mapper;

    public DemandeResponse creer(DemandeRequest request) {
        // TODO S6 : appeler MS1 (GET /api/animaux/{id}) pour vérifier que l'animal est DISPONIBLE
        if (repository.existsByAnimalIdAndClientIdAndStatutIn(
                request.animalId(), request.clientId(), STATUTS_ACTIFS)) {
            throw new ConflictException("Une demande active existe déjà pour cet animal");
        }
        DemandeAdoption demande = new DemandeAdoption();
        demande.setAnimalId(request.animalId());
        demande.setClientId(request.clientId());
        demande = repository.save(demande);
        // TODO S7 : publier DemandeAdoptionCréée
        return mapper.toResponse(demande);
    }

    @Transactional(readOnly = true)
    public List<DemandeResponse> lister() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DemandeResponse trouver(Long id) {
        return mapper.toResponse(chercher(id));
    }

    @Transactional(readOnly = true)
    public List<DemandeResponse> listerParClient(String clientId) {
        return repository.findByClientId(clientId).stream().map(mapper::toResponse).toList();
    }

    public DemandeResponse changerStatut(Long id, StatutRequest request) {
        DemandeAdoption demande = chercher(id);
        StatutDemande cible = request.statut();
        verifierTransition(demande, cible);

        if (cible == REFUSEE) {
            if (request.motifRefus() == null || request.motifRefus().isBlank()) {
                throw new BadRequestException("Le motif de refus est obligatoire");
            }
            demande.setMotifRefus(request.motifRefus());
        }
        if (cible == ACCEPTEE || cible == REFUSEE) {
            demande.setDateDecision(LocalDateTime.now());
        }
        // TODO S6 : si FINALISEE, appeler MS1 (PATCH statut ADOPTE) avant d'enregistrer
        // TODO S7 : publier l'événement correspondant
        demande.setStatut(cible);
        return mapper.toResponse(repository.save(demande));
    }

    public DemandeResponse annuler(Long id) {
        DemandeAdoption demande = chercher(id);
        verifierTransition(demande, ANNULEE);
        demande.setStatut(ANNULEE);
        return mapper.toResponse(repository.save(demande));
    }

    private void verifierTransition(DemandeAdoption demande, StatutDemande cible) {
        Set<StatutDemande> autorises = TRANSITIONS.getOrDefault(demande.getStatut(), Set.of());
        if (!autorises.contains(cible)) {
            throw new ConflictException("Transition interdite : " + demande.getStatut() + " -> " + cible);
        }
    }

    private DemandeAdoption chercher(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande introuvable : " + id));
    }
}