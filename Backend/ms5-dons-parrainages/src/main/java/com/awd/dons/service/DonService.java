package com.awd.dons.service;

import com.awd.dons.common.exception.ConflictException;
import com.awd.dons.common.exception.ResourceNotFoundException;
import com.awd.dons.dto.DonRequest;
import com.awd.dons.dto.DonResponse;
import com.awd.dons.dto.PaiementDonRequest;
import com.awd.dons.dto.PaiementResponse;
import com.awd.dons.entity.Don;
import com.awd.dons.entity.Donateur;
import com.awd.dons.entity.Paiement;
import com.awd.dons.entity.Recu;
import com.awd.dons.entity.StatutDon;
import com.awd.dons.entity.StatutPaiement;
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

    /** Crée le don au statut EN_ATTENTE : il sera validé quand il sera payé. */
    @Transactional
    public DonResponse creer(DonRequest req) {
        Donateur donateur = donateurService.getEntity(req.donateurId());

        // TODO S6 : vérifier l'animal auprès de MS1 (OpenFeign) -> 404 / 503

        Don don = new Don();
        don.setDonateur(donateur);
        don.setAnimalId(req.animalId());
        don.setMontant(req.montant());
        don.setMessage(req.message());
        don.setDateDon(LocalDateTime.now());
        don.setStatut(StatutDon.EN_ATTENTE);
        don = donRepository.save(don);

        return toResponse(don);
    }

    @Transactional(readOnly = true)
    public List<DonResponse> rechercher(Long animalId, Long donateurId) {
        return donRepository.rechercher(animalId, donateurId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DonResponse getById(Long id) {
        return toResponse(getEntity(id));
    }

    /** Paiement d'un don : seulement s'il est EN_ATTENTE. Génère le reçu et valide le don. */
    @Transactional
    public PaiementResponse payerDon(Long id, PaiementDonRequest req) {
        Don don = getEntity(id);
        if (don.getStatut() != StatutDon.EN_ATTENTE) {
            throw new ConflictException("Ce don n'est pas en attente de paiement (statut " + don.getStatut() + ")");
        }

        Paiement paiement = new Paiement();
        paiement.setDon(don);
        paiement.setMontant(don.getMontant());
        paiement.setModePaiement(req.modePaiement());
        paiement.setDatePaiement(LocalDateTime.now());
        paiement.setStatut(StatutPaiement.VALIDE);
        Recu recu = paiementService.enregistrerAvecRecu(paiement);

        don.setStatut(StatutDon.VALIDE);
        donRepository.save(don);

        // TODO S7 : publier DonValidé (clé don.valide)

        return PaiementMapper.toResponse(recu.getPaiement(), recu);
    }

    /** Un don n'est annulable que tant qu'il est EN_ATTENTE (pas encore payé). */
    @Transactional
    public DonResponse annuler(Long id) {
        Don don = getEntity(id);
        if (don.getStatut() != StatutDon.EN_ATTENTE) {
            throw new ConflictException("Seul un don en attente peut être annulé (statut " + don.getStatut() + ")");
        }
        don.setStatut(StatutDon.ANNULE);
        return toResponse(donRepository.save(don));
    }

    /** Retrouve le paiement et le reçu du don (null s'il n'est pas encore payé), puis construit la réponse. */
    private DonResponse toResponse(Don don) {
        Paiement paiement = paiementRepository.findFirstByDonIdOrderByDatePaiementDesc(don.getId()).orElse(null);
        Recu recu = paiement != null
                ? recuRepository.findByPaiementId(paiement.getId()).orElse(null)
                : null;
        return DonMapper.toResponse(don, paiement, recu);
    }

    private Don getEntity(Long id) {
        return donRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Don " + id + " introuvable"));
    }
}