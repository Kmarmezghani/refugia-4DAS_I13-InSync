package com.awd.dons.service;

import com.awd.dons.common.exception.ResourceNotFoundException;
import com.awd.dons.dto.RecuResponse;
import com.awd.dons.entity.Paiement;
import com.awd.dons.entity.Recu;
import com.awd.dons.mapper.PaiementMapper;
import com.awd.dons.repository.PaiementRepository;
import com.awd.dons.repository.RecuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final RecuRepository recuRepository;


    @Transactional
    public Recu enregistrerAvecRecu(Paiement paiement) {
        Paiement enregistre = paiementRepository.save(paiement);

        Recu recu = new Recu();
        recu.setPaiement(enregistre);
        recu.setMontant(enregistre.getMontant());
        recu.setDateEmission(LocalDateTime.now());
        recu.setNumero(String.format("REC-%d-%06d", recu.getDateEmission().getYear(), enregistre.getId()));
        return recuRepository.save(recu);
    }

    @Transactional(readOnly = true)
    public RecuResponse getRecuDuPaiement(Long paiementId) {
        if (!paiementRepository.existsById(paiementId)) {
            throw new ResourceNotFoundException("Paiement " + paiementId + " introuvable");
        }
        Recu recu = recuRepository.findByPaiementId(paiementId)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun reçu pour le paiement " + paiementId));
        return PaiementMapper.toResponse(recu);
    }
}