package com.awd.visites.service;

import com.awd.visites.common.exception.ConflictException;
import com.awd.visites.common.exception.ResourceNotFoundException;
import com.awd.visites.dto.EvenementRequest;
import com.awd.visites.dto.EvenementResponse;
import com.awd.visites.entity.Evenement;
import com.awd.visites.entity.StatutEvenement;
import com.awd.visites.mapper.EvenementMapper;
import com.awd.visites.repository.EvenementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvenementService {

    private final EvenementRepository evenementRepository;
    private final EvenementMapper mapper;

    @Transactional
    public EvenementResponse creer(EvenementRequest request) {
        Evenement evenement = evenementRepository.save(mapper.toEntity(request));
        return mapper.toResponse(evenement);
    }

    @Transactional(readOnly = true)
    public List<EvenementResponse> lister(boolean aVenir) {
        List<Evenement> evenements = aVenir
                ? evenementRepository.findByDateGreaterThanEqualOrderByDateAsc(LocalDateTime.now())
                : evenementRepository.findAllByOrderByDateAsc();
        return evenements.stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public EvenementResponse annuler(Long id) {
        Evenement evenement = evenementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement introuvable : " + id));
        if (evenement.getStatut() == StatutEvenement.ANNULE) {
            throw new ConflictException("Cet événement est déjà annulé");
        }
        // TODO (plus tard) : annuler les inscriptions, publier l'événement EvenementAnnulé.
        evenement.setStatut(StatutEvenement.ANNULE);
        return mapper.toResponse(evenementRepository.save(evenement));
    }
}