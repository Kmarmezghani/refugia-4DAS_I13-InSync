package com.awd.adoptions.controller;

import com.awd.adoptions.dto.DemandeRequest;
import com.awd.adoptions.dto.DemandeResponse;
import com.awd.adoptions.dto.StatutRequest;
import com.awd.adoptions.service.DemandeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandes")
@RequiredArgsConstructor
public class DemandeController {

    private final DemandeService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeResponse creer(@Valid @RequestBody DemandeRequest request) {
        return service.creer(request);
    }

    @GetMapping
    public List<DemandeResponse> lister() {
        return service.lister();
    }

    @GetMapping("/{id}")
    public DemandeResponse trouver(@PathVariable Long id) {
        return service.trouver(id);
    }

    @GetMapping("/client/{clientId}")
    public List<DemandeResponse> listerParClient(@PathVariable String clientId) {
        return service.listerParClient(clientId);
    }

    @PutMapping("/{id}/statut")
    public DemandeResponse changerStatut(@PathVariable Long id, @Valid @RequestBody StatutRequest request) {
        return service.changerStatut(id, request);
    }

    @DeleteMapping("/{id}")
    public DemandeResponse annuler(@PathVariable Long id) {
        return service.annuler(id);
    }
}