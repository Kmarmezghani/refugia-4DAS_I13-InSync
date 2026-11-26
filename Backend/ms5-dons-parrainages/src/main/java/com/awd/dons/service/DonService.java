package com.awd.dons.service;

import com.awd.dons.common.exception.ConflictException;
import com.awd.dons.common.exception.ResourceNotFoundException;
import com.awd.dons.dto.DonRequest;
import com.awd.dons.dto.DonResponse;
import com.awd.dons.dto.RecuResponse;
import com.awd.dons.entity.*;
import com.awd.dons.mapper.DonMapper;
import com.awd.dons.mapper.PaiementMapper;
import com.awd.dons.repository.DonRepository;
import com.awd.dons.repository.PaiementRepository;
import com.awd.dons.repository.RecuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonService {

    private final DonRepository donRepository;
    private final PaiementRepository paiementRepository;
    private final RecuRepository recuRepository;
    private final DonateurService donateurService;
    private final PaiementService paiementService;


    @Transactional
    public DonResponse creer(DonRequest req) {
        Donateur donateur = donateurService.getEntity(req.donateurId());


        Don don = new Don();
        don.setDonateur(donateur);
        don.setAnimalId(req.animalId());
        don.setMontant(req.montant());
        don.setMessage(req.message());
        don.setDateDon(LocalDateTime.now());
        don.setStatut(StatutDon.EN_ATTENTE);
        don = donRepository.save(don);

        Paiement paiement = new Paiement();
        paiement.setDon(don);
        paiement.setMontant(req.montant());
        paiement.setModePaiement(req.modePaiement());
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setStatut(StatutPaiement.VALIDE);
        Recu recu = paiementService.enregistrerAvecRecu(paiement);

        don.setStatut(StatutDon.VALIDE);
        donRepository.save(don);


        return DonMapper.toResponse(don, recu.getPaiement(), recu);
    }

    @Transactional(readOnly = true)
    public List<DonResponse> rechercher(Long animalId, Long donateurId) {
        return donRepository.rechercher(animalId, donateurId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DonResponse getById(Long id) {
        return toResponse(getEntity(id));
    }

    @Transactional(readOnly = true)
    public List<DonResponse> donsDuDonateur(Long donateurId) {
        donateurService.getEntity(donateurId); // 404 si le donateur n'existe pas
        return donRepository.findByDonateurIdOrderByDateDonDesc(donateurId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public RecuResponse getRecu(Long donId) {
        getEntity(donId);
        Paiement paiement = paiementRepository.findFirstByDonId(donId)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun paiement pour le don " + donId));
        Recu recu = recuRepository.findByPaiementId(paiement.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Aucun reçu pour le don " + donId));
        return PaiementMapper.toResponse(recu);
    }

    @Transactional
    public DonResponse annuler(Long id) {
        Don don = getEntity(id);
        if (don.getStatut() == StatutDon.ANNULE) {
            throw new ConflictException("Le don " + id + " est déjà annulé");
        }
        don.setStatut(StatutDon.ANNULE);
        return toResponse(donRepository.save(don));
    }

    private Don getEntity(Long id) {
        return donRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Don " + id + " introuvable"));
    }

    private DonResponse toResponse(Don don) {
        Paiement paiement = paiementRepository.findFirstByDonId(don.getId()).orElse(null);
        Recu recu = paiement != null ? recuRepository.findByPaiementId(paiement.getId()).orElse(null) : null;
        return DonMapper.toResponse(don, paiement, recu);
    }
}
