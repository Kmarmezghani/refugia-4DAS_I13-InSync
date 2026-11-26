package com.awd.dons.controller;

import com.awd.dons.dto.DonRequest;
import com.awd.dons.dto.DonResponse;
import com.awd.dons.service.DonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dons")
@RequiredArgsConstructor
@Tag(name = "Dons")
public class DonController {

    private final DonService donService;

    @PostMapping
    @Operation(summary = "Enregistrer un don")
    public ResponseEntity<DonResponse> creer(@Valid @RequestBody DonRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(donService.creer(req));
    }

    @GetMapping
    @Operation(summary = "Lister et filtrer les dons")
    public List<DonResponse> rechercher(@RequestParam(required = false) Long animalId,
                                        @RequestParam(required = false) Long donateurId) {
        return donService.rechercher(animalId, donateurId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un don")
    public DonResponse getById(@PathVariable Long id) {
        return donService.getById(id);
    }

    @PatchMapping("/{id}/annuler")
    @Operation(summary = "Annuler un don")
    public DonResponse annuler(@PathVariable Long id) {
        return donService.annuler(id);
    }
}