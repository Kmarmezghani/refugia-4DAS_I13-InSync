package com.awd.dons.service;

import com.awd.dons.common.exception.ResourceNotFoundException;
import com.awd.dons.dto.PaiementResponse;
import com.awd.dons.entity.Paiement;
import com.awd.dons.mapper.PaiementMapper;
import com.awd.dons.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;


    @Transactional
    public Paiement enregistrer(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    @Transactional(readOnly = true)
    public PaiementResponse getById(Long paiementId) {
        Paiement paiement = paiementRepository.findById(paiementId)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement " + paiementId + " introuvable"));
        return PaiementMapper.toResponse(paiement);
    }
}