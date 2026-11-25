package com.awd.veterinaire.controller;

import com.awd.veterinaire.dto.DossierMedicalResponse;
import com.awd.veterinaire.dto.ModificationNotesRequest;
import com.awd.veterinaire.dto.OuvertureDossierRequest;
import com.awd.veterinaire.service.DossierMedicalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vet/records")
@RequiredArgsConstructor
@Tag(name = "Dossiers médicaux")
public class DossierMedicalController {

    private final DossierMedicalService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DossierMedicalResponse ouvrir(@Valid @RequestBody OuvertureDossierRequest request) {
        return service.ouvrir(request.animalId());
    }

    @GetMapping("/{id}")
    public DossierMedicalResponse lire(@PathVariable Long id) {
        return service.lire(id);
    }

    @GetMapping("/by-animal/{animalId}")
    public DossierMedicalResponse lireParAnimal(@PathVariable Long animalId) {
        return service.lireParAnimal(animalId);
    }

    @PatchMapping("/{id}")
    public DossierMedicalResponse modifierNotes(
            @PathVariable Long id, @RequestBody ModificationNotesRequest request) {
        return service.modifierNotes(id, request.notesGenerales());
    }
}
