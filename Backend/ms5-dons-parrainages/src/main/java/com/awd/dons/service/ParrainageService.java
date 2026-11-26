package com.awd.dons.service;

import com.awd.dons.common.exception.ConflictException;
import com.awd.dons.common.exception.ResourceNotFoundException;
import com.awd.dons.dto.*;
import com.awd.dons.entity.*;
import com.awd.dons.mapper.PaiementMapper;
import com.awd.dons.mapper.ParrainageMapper;
import com.awd.dons.repository.PaiementRepository;
import com.awd.dons.repository.ParrainageRepository;
import com.awd.dons.repository.RecuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParrainageService {

    private static final List<StatutParrainage> EN_COURS = List.of(StatutParrainage.ACTIF, StatutParrainage.SUSPENDU);

    private final ParrainageRepository parrainageRepository;
    private final PaiementRepository paiementRepository;
    private final RecuRepository recuRepository;
    private final DonateurService donateurService;
    private final PaiementService paiementService;

    @Transactional
    public ParrainageResponse creer(ParrainageRequest req) {
        Donateur donateur = donateurService.getEntity(req.donateurId());


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
        }
        return ParrainageMapper.toResponse(parrainageRepository.save(p));
    }

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
        Recu recu = paiementService.enregistrerAvecRecu(paiement);

        return PaiementMapper.toResponse(recu.getPaiement(), recu);
    }

    @Transactional(readOnly = true)
    public List<PaiementResponse> paiements(Long id) {
        getEntity(id);
        return paiementRepository.findByParrainageIdOrderByDatePaiementDesc(id).stream()
                .map(pa -> PaiementMapper.toResponse(pa, recuRepository.findByPaiementId(pa.getId()).orElse(null)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ParrainageResponse> parrainsDeLAnimal(Long animalId) {
        return parrainageRepository.findByAnimalIdAndStatutIn(animalId, List.of(StatutParrainage.ACTIF)).stream()
                .map(ParrainageMapper::toResponse)
                .toList();
    }


    @Transactional
    public int terminerPourAdoption(Long animalId) {
        List<Parrainage> enCours = parrainageRepository.findByAnimalIdAndStatutIn(animalId, EN_COURS);
        for (Parrainage p : enCours) {
            p.setStatut(StatutParrainage.TERMINE);
            p.setDateFin(LocalDate.now());
        }
        parrainageRepository.saveAll(enCours);
        return enCours.size();
    }

    private Parrainage getEntity(Long id) {
        return parrainageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parrainage " + id + " introuvable"));
    }
}