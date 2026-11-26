package com.awd.dons.service;

import com.awd.dons.common.exception.ConflictException;
import com.awd.dons.common.exception.ResourceNotFoundException;
import com.awd.dons.dto.*;
import com.awd.dons.entity.*;
import com.awd.dons.mapper.ParrainageMapper;
import com.awd.dons.repository.ParrainageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParrainageService {

    private static final List<StatutParrainage> EN_COURS = List.of(StatutParrainage.ACTIF, StatutParrainage.SUSPENDU);

    private final ParrainageRepository parrainageRepository;
    private final DonateurService donateurService;

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