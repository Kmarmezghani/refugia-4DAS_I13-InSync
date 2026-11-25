package com.awd.veterinaire.service;

import com.awd.veterinaire.dto.DossierMedicalResponse;
import com.awd.veterinaire.entity.DossierMedical;
import com.awd.veterinaire.mapper.DossierMedicalMapper;
import com.awd.veterinaire.repository.DossierMedicalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DossierMedicalService {

    private final DossierMedicalRepository repository;
    private final DossierMedicalMapper mapper;
    private final VerificationAnimal verificationAnimal;

    @Transactional
    public DossierMedicalResponse ouvrir(Long animalId) {
        verificationAnimal.verifierExistence(animalId);
        if (repository.existsByAnimalId(animalId)) {
            throw dossierDejaOuvert(animalId);
        }

        String numeroDossier = String.format(Locale.ROOT, "VET-%04d", animalId);
        DossierMedical dossier = new DossierMedical(animalId, numeroDossier, LocalDate.now());
        try {
            return mapper.versResponse(repository.saveAndFlush(dossier));
        } catch (DataIntegrityViolationException exception) {
            // La contrainte unique protège aussi deux ouvertures simultanées.
            if (exception.getMostSpecificCause() instanceof SQLException sqlException
                    && "23505".equals(sqlException.getSQLState())) {
                throw dossierDejaOuvert(animalId);
            }
            throw exception;
        }
    }

    public DossierMedicalResponse lire(Long id) {
        return mapper.versResponse(trouverDossier(id));
    }

    public DossierMedicalResponse lireParAnimal(Long animalId) {
        return repository.findByAnimalId(animalId)
                .map(mapper::versResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Aucun dossier médical pour l'animal " + animalId));
    }

    @Transactional
    public DossierMedicalResponse modifierNotes(Long id, String notesGenerales) {
        DossierMedical dossier = trouverDossier(id);
        dossier.modifierNotesGenerales(notesGenerales);
        return mapper.versResponse(dossier);
    }

    DossierMedical trouverDossier(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Dossier médical introuvable : " + id));
    }

    private ResponseStatusException dossierDejaOuvert(Long animalId) {
        return new ResponseStatusException(HttpStatus.CONFLICT,
                "Un dossier médical existe déjà pour l'animal " + animalId);
    }
}
