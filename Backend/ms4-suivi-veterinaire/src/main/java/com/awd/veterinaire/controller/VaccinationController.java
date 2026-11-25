package com.awd.veterinaire.controller;

import com.awd.veterinaire.dto.CorrectionVaccinationRequest;
import com.awd.veterinaire.dto.VaccinationRequest;
import com.awd.veterinaire.dto.VaccinationResponse;
import com.awd.veterinaire.entity.StatutVaccination;
import com.awd.veterinaire.service.VaccinationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vet")
@RequiredArgsConstructor
@Tag(name = "Vaccinations")
public class VaccinationController {

    private final VaccinationService service;

    @PostMapping("/vaccinations")
    @ResponseStatus(HttpStatus.CREATED)
    public VaccinationResponse enregistrer(@Valid @RequestBody VaccinationRequest request) {
        return service.enregistrer(request);
    }

    @GetMapping("/records/{id}/vaccinations")
    public List<VaccinationResponse> historique(@PathVariable Long id) {
        return service.historique(id);
    }

    @GetMapping("/vaccinations/reminders")
    public List<VaccinationResponse> rappels(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beforeDate,
            @RequestParam(required = false) StatutVaccination statut) {
        return service.rappels(beforeDate, statut);
    }

    @PatchMapping("/vaccinations/{id}")
    public VaccinationResponse corriger(@PathVariable Long id,
            @Valid @RequestBody CorrectionVaccinationRequest request) {
        return service.corriger(id, request);
    }
}
