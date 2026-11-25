package com.awd.visites.service;

import com.awd.visites.common.exception.ConflictException;
import com.awd.visites.common.exception.ResourceNotFoundException;
import com.awd.visites.dto.InscriptionRequest;
import com.awd.visites.dto.InscriptionResponse;
import com.awd.visites.entity.Evenement;
import com.awd.visites.entity.Inscription;
import com.awd.visites.entity.StatutEvenement;
import com.awd.visites.entity.StatutInscription;
import com.awd.visites.mapper.InscriptionMapper;
import com.awd.visites.repository.EvenementRepository;
import com.awd.visites.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EvenementRepository evenementRepository;
    private final InscriptionMapper mapper;

    @Transactional
    public InscriptionResponse inscrire(Long evenementId, InscriptionRequest request) {
        Evenement evenement = trouverEvenement(evenementId);
        if (evenement.getStatut() == StatutEvenement.ANNULE) {
            throw new ConflictException("Impossible de s'inscrire à un événement annulé");
        }
        if (inscriptionRepository.existsByEvenementIdAndParticipantIdAndStatut(
                evenementId, request.participantId(), StatutInscription.INSCRIT)) {
            throw new ConflictException("Ce participant est déjà inscrit à cet événement");
        }
        // TODO (plus tard) : gérer les places restantes et la liste d'attente (EN_ATTENTE).
        Inscription inscription = inscriptionRepository.save(mapper.toEntity(evenementId, request));
        return mapper.toResponse(inscription);
    }

    @Transactional(readOnly = true)
    public List<InscriptionResponse> lister(Long evenementId) {
        trouverEvenement(evenementId);
        return inscriptionRepository.findByEvenementIdOrderByDateInscriptionAsc(evenementId)
                .stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public void desinscrire(Long evenementId, Long inscriptionId) {
        trouverEvenement(evenementId);
        Inscription inscription = inscriptionRepository.findById(inscriptionId)
                .filter(i -> i.getEvenementId().equals(evenementId))
                .orElseThrow(() -> new ResourceNotFoundException("Inscription introuvable : " + inscriptionId));
        // TODO (plus tard) : libérer la place et promouvoir le premier de la liste d'attente.
        inscription.setStatut(StatutInscription.ANNULEE);
        inscriptionRepository.save(inscription);
    }

    private Evenement trouverEvenement(Long id) {
        return evenementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement introuvable : " + id));
    }
}