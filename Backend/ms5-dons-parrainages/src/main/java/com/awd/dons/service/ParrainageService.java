package com.awd.dons.service;

import com.awd.dons.common.exception.ConflictException;
import com.awd.dons.common.exception.ResourceNotFoundException;
import com.awd.dons.dto.*;
import com.awd.dons.entity.*;
import com.awd.dons.mapper.PaiementMapper;
import com.awd.dons.mapper.ParrainageMapper;
import com.awd.dons.repository.PaiementRepository;
import com.awd.dons.repository.ParrainageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParrainageService {

    /** Un parrainage "en cours" est ACTIF ou SUSPENDU (pas encore TERMINE). */
    private static final List<StatutParrainage> EN_COURS = List.of(StatutParrainage.ACTIF, StatutParrainage.SUSPENDU);

    private final ParrainageRepository parrainageRepository;
    private final PaiementRepository paiementRepository;
    private final DonateurService donateurService;
    private final PaiementService paiementService;

    @Transactional
    public ParrainageResponse creer(ParrainageRequest req) {
        Donateur donateur = donateurService.getEntity(req.donateurId());

        // TODO S6 : vérifier l'animal auprès de MS1 (OpenFeign) -> 404 / 503

        if (parrainageRepository.existsByDonateurIdAndAnimalIdAndStatutIn(donateur.getId(), req.animalId(), EN_COURS)) {
            throw new ConflictException("Ce donateur parraine déjà l'animal " + req.animalId());
        }

        Parrainage p = new Parrainage();
        p.setDonateur(donateur);
        p.setAnimalId(req.animalId());
        p.setMontantMensuel(req.montantMensuel());
        p.setDateDebut(LocalDate.now());
        p.setStatut(StatutParrainage.ACTIF);
        p = parrainageRepository.save(p);

        // TODO S7 : publier ParrainageCréé (clé parrainage.cree)

        return ParrainageMapper.toResponse(p);
    }

    @Transactional(readOnly = true)
    public List<ParrainageResponse> rechercher(Long animalId, StatutParrainage statut) {
        return parrainageRepository.rechercher(animalId, statut).stream().map(ParrainageMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ParrainageResponse getById(Long id) {
        return ParrainageMapper.toResponse(getEntity(id));
    }

    /**
     * Transitions autorisées :
     *   ACTIF -> SUSPENDU, SUSPENDU -> ACTIF, ACTIF/SUSPENDU -> TERMINE.
     *   Un parrainage TERMINE ne change plus (409).
     */
    @Transactional
    public ParrainageResponse changerStatut(Long id, ChangementStatutRequest req) {
        Parrainage p = getEntity(id);
        StatutParrainage actuel = p.getStatut();
        StatutParrainage nouveau = req.statut();

        if (actuel == StatutParrainage.TERMINE) {
            throw new ConflictException("Le parrainage " + id + " est déjà terminé");
        }
        if (actuel == nouveau) {
            throw new ConflictException("Le parrainage est déjà au statut " + actuel);
        }

        p.setStatut(nouveau);
        if (nouveau == StatutParrainage.TERMINE) {
            p.setDateFin(LocalDate.now());
            // TODO S7 : publier ParrainageTerminé (clé parrainage.termine) avec req.motif()
        }
        return ParrainageMapper.toResponse(parrainageRepository.save(p));
    }

    /** Paiement d'un mois : seulement si ACTIF, et un seul paiement par mois. */
    @Transactional
    public PaiementResponse payerMois(Long id, PaiementParrainageRequest req) {
        Parrainage p = getEntity(id);
        if (p.getStatut() != StatutParrainage.ACTIF) {
            throw new ConflictException("Le parrainage n'est pas actif (statut " + p.getStatut() + ")");
        }

        LocalDateTime debutMois = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime finMois = debutMois.plusMonths(1).minusNanos(1);
        if (paiementRepository.existsByParrainageIdAndStatutAndDatePaiementBetween(
                id, StatutPaiement.VALIDE, debutMois, finMois)) {
            throw new ConflictException("Le mois en cours est déjà payé pour ce parrainage");
        }

        Paiement paiement = new Paiement();
        paiement.setParrainage(p);
        paiement.setMontant(p.getMontantMensuel());
        paiement.setModePaiement(req.modePaiement());
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setStatut(StatutPaiement.VALIDE);

        return PaiementMapper.toResponse(paiementService.enregistrer(paiement));
    }

    @Transactional(readOnly = true)
    public List<PaiementResponse> paiements(Long id) {
        getEntity(id);
        return paiementRepository.findByParrainageIdOrderByDatePaiementDesc(id).stream()
                .map(PaiementMapper::toResponse)
                .toList();
    }

    /** GET /api/animaux/{animalId}/parrains : les parrainages ACTIF d'un animal. */
    @Transactional(readOnly = true)
    public List<ParrainageResponse> parrainsDeLAnimal(Long animalId) {
        // TODO S6 : 404 si l'animal n'existe pas dans MS1
        return parrainageRepository.findByAnimalIdAndStatutIn(animalId, List.of(StatutParrainage.ACTIF)).stream()
                .map(ParrainageMapper::toResponse)
                .toList();
    }

    /**
     * Sera appelé en S7 par l'écouteur de l'événement AdoptionFinalisée (MS2) :
     * l'animal est adopté, donc ses parrainages en cours se terminent.
     */
    @Transactional
    public int terminerPourAdoption(Long animalId) {
        List<Parrainage> enCours = parrainageRepository.findByAnimalIdAndStatutIn(animalId, EN_COURS);
        for (Parrainage p : enCours) {
            p.setStatut(StatutParrainage.TERMINE);
            p.setDateFin(LocalDate.now());
            // TODO S7 : publier ParrainageTerminé avec motif "Animal adopté"
        }
        parrainageRepository.saveAll(enCours);
        return enCours.size();
    }

    private Parrainage getEntity(Long id) {
        return parrainageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parrainage " + id + " introuvable"));
    }
}