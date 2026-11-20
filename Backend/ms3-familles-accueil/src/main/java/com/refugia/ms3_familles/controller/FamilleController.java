package com.refugia.ms3_familles.controller;

import com.refugia.ms3_familles.dto.FamilleRequest;
import com.refugia.ms3_familles.entity.FamilleAccueil;
import com.refugia.ms3_familles.service.FamilleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}