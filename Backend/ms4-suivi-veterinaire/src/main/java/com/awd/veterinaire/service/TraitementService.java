package com.awd.veterinaire.service;

import com.awd.veterinaire.dto.TraitementRequest;
import com.awd.veterinaire.dto.TraitementResponse;
import com.awd.veterinaire.entity.DossierMedical;
import com.awd.veterinaire.entity.StatutTraitement;
import com.awd.veterinaire.entity.Traitement;
import com.awd.veterinaire.mapper.TraitementMapper;
import com.awd.veterinaire.repository.TraitementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TraitementService {

    private final TraitementRepository repository;
    private final DossierMedicalService dossiers;
    private final TraitementMapper mapper;

    @Transactional
    public TraitementResponse creer(TraitementRequest request) {
        if (request.dateFin() != null && request.dateFin().isBefore(request.dateDebut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La date de fin doit être égale ou postérieure au début du traitement.");
        }
        DossierMedical dossier = dossiers.trouverDossier(request.dossierId());
        Traitement traitement = new Traitement(dossier, request.medicament(), request.posologie(),
                request.dateDebut(), request.dateFin());
        return mapper.versResponse(repository.save(traitement));
    }

    public List<TraitementResponse> lister(Long dossierId) {
        dossiers.trouverDossier(dossierId);
        return repository.findAllByDossier_IdOrderByDateDebutDescIdDesc(dossierId)
                .stream().map(mapper::versResponse).toList();
    }

    @Transactional
    public TraitementResponse changerStatut(Long id, StatutTraitement cible) {
        Traitement traitement = repository.verrouillerParId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Traitement introuvable : " + id));
        if (!traitement.getStatut().peutPasserA(cible)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Transition de " + traitement.getStatut() + " vers " + cible + " interdite.");
        }
        traitement.changerStatut(cible);
        return mapper.versResponse(traitement);
    }
}
