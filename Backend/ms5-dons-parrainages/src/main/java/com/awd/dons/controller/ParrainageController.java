package com.awd.dons.controller;

import com.awd.dons.dto.*;
import com.awd.dons.entity.StatutParrainage;
import com.awd.dons.service.ParrainageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parrainages")
@RequiredArgsConstructor
@Tag(name = "Parrainages")
public class ParrainageController {

    private final ParrainageService parrainageService;

    @PostMapping
    @Operation(summary = "Créer un parrainage")
    public ResponseEntity<ParrainageResponse> creer(@Valid @RequestBody ParrainageRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parrainageService.creer(req));
    }

    @GetMapping
    @Operation(summary = "Lister et filtrer les parrainages")
    public List<ParrainageResponse> rechercher(@RequestParam(required = false) Long animalId,
                                               @RequestParam(required = false) StatutParrainage statut) {
        return parrainageService.rechercher(animalId, statut);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un parrainage")
    public ParrainageResponse getById(@PathVariable Long id) {
        return parrainageService.getById(id);
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Suspendre, reprendre ou terminer un parrainage")
    public ParrainageResponse changerStatut(@PathVariable Long id, @Valid @RequestBody ChangementStatutRequest req) {
        return parrainageService.changerStatut(id, req);
    }

   /* @PostMapping("/{id}/paiements")
    @Operation(summary = "Enregistrer le paiement du mois (génère un reçu)")
    public ResponseEntity<PaiementResponse> payer(@PathVariable Long id, @Valid @RequestBody PaiementParrainageRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parrainageService.payerMois(id, req));
    }

    @GetMapping("/{id}/paiements")
    @Operation(summary = "Lister les paiements d'un parrainage")
    public List<PaiementResponse> paiements(@PathVariable Long id) {
        return parrainageService.paiements(id);
    }*/
}
