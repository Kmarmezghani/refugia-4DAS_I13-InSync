package com.awd.veterinaire.service;

import com.awd.veterinaire.dto.CorrectionVaccinationRequest;
import com.awd.veterinaire.dto.VaccinationRequest;
import com.awd.veterinaire.dto.VaccinationResponse;
import com.awd.veterinaire.entity.DossierMedical;
import com.awd.veterinaire.entity.StatutVaccination;
import com.awd.veterinaire.entity.Vaccination;
import com.awd.veterinaire.mapper.VaccinationMapper;
import com.awd.veterinaire.repository.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VaccinationService {

    private final VaccinationRepository repository;
    private final DossierMedicalService dossiers;
    private final VaccinationMapper mapper;
    private final Clock horloge;

    @Transactional
    public VaccinationResponse enregistrer(VaccinationRequest request) {
        verifierDates(request.dateVaccination(), request.dateRappel());
        DossierMedical dossier = dossiers.trouverDossier(request.dossierId());
        LocalDate aujourdhui = LocalDate.now(horloge);
        Vaccination vaccination = new Vaccination(dossier, request.vaccin(), request.dateVaccination(),
                request.dateRappel(), aujourdhui);
        return mapper.versResponse(repository.save(vaccination), aujourdhui);
    }

    public List<VaccinationResponse> historique(Long dossierId) {
        dossiers.trouverDossier(dossierId);
        LocalDate aujourdhui = LocalDate.now(horloge);
        return repository.findAllByDossier_IdOrderByDateVaccinationDescIdDesc(dossierId)
                .stream().map(vaccination -> mapper.versResponse(vaccination, aujourdhui)).toList();
    }

    public List<VaccinationResponse> rappels(LocalDate beforeDate, StatutVaccination statut) {
        LocalDate aujourdhui = LocalDate.now(horloge);
        List<Vaccination> rappels = beforeDate == null
                ? repository.findAllByDateRappelIsNotNullOrderByDateRappelAscIdAsc()
                : repository.findAllByDateRappelBeforeOrderByDateRappelAscIdAsc(beforeDate);
        return rappels.stream()
                .map(vaccination -> mapper.versResponse(vaccination, aujourdhui))
                .filter(vaccination -> statut == null || vaccination.statut() == statut)
                .toList();
    }

    @Transactional
    public VaccinationResponse corriger(Long id, CorrectionVaccinationRequest request) {
        Vaccination vaccination = repository.verrouillerParId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Vaccination introuvable : " + id));
        String vaccin = request.getVaccin() != null ? request.getVaccin() : vaccination.getVaccin();
        LocalDate dateVaccination = request.getDateVaccination() != null
                ? request.getDateVaccination() : vaccination.getDateVaccination();
        LocalDate dateRappel = request.isDateRappelModifiee() ? request.getDateRappel() : vaccination.getDateRappel();
        verifierDates(dateVaccination, dateRappel);
        LocalDate aujourdhui = LocalDate.now(horloge);
        vaccination.corriger(vaccin, dateVaccination, dateRappel, aujourdhui);
        return mapper.versResponse(vaccination, aujourdhui);
    }

    private void verifierDates(LocalDate dateVaccination, LocalDate dateRappel) {
        if (dateRappel != null && dateRappel.isBefore(dateVaccination)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La date de rappel doit être égale ou postérieure à la vaccination.");
        }
    }
}
