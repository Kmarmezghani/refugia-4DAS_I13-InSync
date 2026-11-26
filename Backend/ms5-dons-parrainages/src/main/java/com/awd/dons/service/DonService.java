package com.awd.dons.service;

import com.awd.dons.common.exception.ConflictException;
import com.awd.dons.common.exception.ResourceNotFoundException;
import com.awd.dons.dto.DonRequest;
import com.awd.dons.dto.DonResponse;
import com.awd.dons.entity.Don;
import com.awd.dons.entity.Donateur;
import com.awd.dons.entity.StatutDon;
import com.awd.dons.mapper.DonMapper;
import com.awd.dons.repository.DonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonService {

    private final DonRepository donRepository;
    private final DonateurService donateurService;

    @Transactional
    public DonResponse creer(DonRequest req) {
        Donateur donateur = donateurService.getEntity(req.donateurId());

        // TODO S6 : si animalId != null, vérifier l'animal auprès de MS1 (OpenFeign) -> 404 / 503

        Don don = new Don();
        don.setDonateur(donateur);
        don.setAnimalId(req.animalId());
        don.setMontant(req.montant());
        don.setMessage(req.message());
        don.setDateDon(LocalDateTime.now());
        don.setStatut(StatutDon.VALIDE);
        don = donRepository.save(don);

        // TODO S7 : publier l'événement DonValidé (clé don.valide) dans RabbitMQ

        return DonMapper.toResponse(don);
    }

    @Transactional(readOnly = true)
    public List<DonResponse> rechercher(Long animalId, Long donateurId) {
        return donRepository.rechercher(animalId, donateurId).stream()
                .map(DonMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DonResponse getById(Long id) {
        return DonMapper.toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public List<DonResponse> donsDuDonateur(Long donateurId) {
        donateurService.getEntity(donateurId); // 404 si le donateur n'existe pas
        return donRepository.findByDonateurIdOrderByDateDonDesc(donateurId).stream()
                .map(DonMapper::toResponse)
                .toList();
    }

    @Transactional
    public DonResponse annuler(Long id) {
        Don don = getEntity(id);
        if (don.getStatut() == StatutDon.ANNULE) {
            throw new ConflictException("Le don " + id + " est déjà annulé");
        }
        don.setStatut(StatutDon.ANNULE);
        return DonMapper.toResponse(donRepository.save(don));
    }

    private Don getEntity(Long id) {
        return donRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Don " + id + " introuvable"));
    }
}