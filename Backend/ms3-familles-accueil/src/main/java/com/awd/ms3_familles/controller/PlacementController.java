package com.awd.ms3_familles.controller;

import com.awd.ms3_familles.dto.PlacementRequest;
import com.awd.ms3_familles.entity.Placement;
import com.awd.ms3_familles.enums.IssuePlacement;
import com.awd.ms3_familles.enums.StatutPlacement;
import com.awd.ms3_familles.service.PlacementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/placements")
public class PlacementController {

    private final PlacementService service;

    public PlacementController(PlacementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Placement> createPlacement(@Valid @RequestBody PlacementRequest request) {
        Placement nouveauPlacement = service.creerPlacement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauPlacement);
    }
    @GetMapping
    public ResponseEntity<List<Placement>> getAllPlacements(
            @RequestParam(required = false) StatutPlacement statut,
            @RequestParam(required = false) Long familleId,
            @RequestParam(required = false) Long animalId) {
        return ResponseEntity.ok(service.getPlacements(statut, familleId, animalId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Placement> getPlacementById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPlacementById(id));
    }
    @PatchMapping("/{id}/terminer")
    public ResponseEntity<Placement> terminerPlacement(
            @PathVariable Long id,
            @RequestParam IssuePlacement issue) {
        return ResponseEntity.ok(service.terminerPlacement(id, issue));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<Placement> annulerPlacement(@PathVariable Long id) {
        return ResponseEntity.ok(service.annulerPlacement(id));
    }
}
