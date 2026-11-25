package com.awd.veterinaire.controller;

import com.awd.veterinaire.dto.ConsultationRequest;
import com.awd.veterinaire.dto.ConsultationResponse;
import com.awd.veterinaire.service.ConsultationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vet")
@RequiredArgsConstructor
@Tag(name = "Consultations")
public class ConsultationController {

    private final ConsultationService service;

    @PostMapping("/consultations")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultationResponse enregistrer(@Valid @RequestBody ConsultationRequest request) {
        return service.enregistrer(request);
    }

    @GetMapping("/consultations/{id}")
    public ConsultationResponse lire(@PathVariable Long id) {
        return service.lire(id);
    }

    @GetMapping("/records/{id}/consultations")
    public List<ConsultationResponse> historique(@PathVariable Long id) {
        return service.historique(id);
    }
}
