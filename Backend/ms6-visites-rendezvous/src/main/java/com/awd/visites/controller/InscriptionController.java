package com.awd.visites.controller;

import com.awd.visites.dto.InscriptionRequest;
import com.awd.visites.dto.InscriptionResponse;
import com.awd.visites.service.InscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evenements/{evenementId}/inscriptions")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InscriptionResponse inscrire(@PathVariable Long evenementId,
                                        @Valid @RequestBody InscriptionRequest request) {
        return inscriptionService.inscrire(evenementId, request);
    }

    @GetMapping
    public List<InscriptionResponse> lister(@PathVariable Long evenementId) {
        return inscriptionService.lister(evenementId);
    }

    @DeleteMapping("/{inscriptionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desinscrire(@PathVariable Long evenementId,
                            @PathVariable Long inscriptionId) {
        inscriptionService.desinscrire(evenementId, inscriptionId);
    }
}