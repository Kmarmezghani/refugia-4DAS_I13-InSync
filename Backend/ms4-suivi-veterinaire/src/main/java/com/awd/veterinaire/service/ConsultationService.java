package com.awd.veterinaire.service;

import com.awd.veterinaire.dto.ConsultationRequest;
import com.awd.veterinaire.dto.ConsultationResponse;
import com.awd.veterinaire.entity.DossierMedical;
import com.awd.veterinaire.mapper.ConsultationMapper;
import com.awd.veterinaire.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsultationService {

    private final ConsultationRepository repository;
    private final DossierMedicalService dossiers;
    private final ConsultationMapper mapper;

    @Transactional
    public ConsultationResponse enregistrer(ConsultationRequest request) {
        DossierMedical dossier = dossiers.trouverDossier(request.dossierId());
        return mapper.versResponse(repository.save(mapper.versEntite(request, dossier)));
    }

    public ConsultationResponse lire(Long id) {
        return repository.findById(id)
                .map(mapper::versResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Consultation introuvable : " + id));
    }

    public List<ConsultationResponse> historique(Long dossierId) {
        dossiers.trouverDossier(dossierId);
        return repository.findAllByDossier_IdOrderByDateConsultationDescIdDesc(dossierId)
                .stream()
                .map(mapper::versResponse)
                .toList();
    }
}
