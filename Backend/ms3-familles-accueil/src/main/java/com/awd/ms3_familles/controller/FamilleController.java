package com.awd.ms3_familles.controller;

import com.awd.ms3_familles.dto.FamilleRequest;
import com.awd.ms3_familles.entity.FamilleAccueil;
import com.awd.ms3_familles.enums.StatutFamille;
import com.awd.ms3_familles.service.FamilleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/familles")
public class FamilleController {

    private final FamilleService service;

    public FamilleController(FamilleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FamilleAccueil> createFamille(@Valid @RequestBody FamilleRequest request) {
        FamilleAccueil nouvelleFamille = service.creerFamille(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleFamille);
    }

    @GetMapping
    public ResponseEntity<List<FamilleAccueil>> getAllFamilles(
            @RequestParam(required = false) StatutFamille statut,
            @RequestParam(required = false) String espece) {
        return ResponseEntity.ok(service.getFamilles(statut, espece));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamilleAccueil> getFamilleById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFamilleById(id));
    }
}