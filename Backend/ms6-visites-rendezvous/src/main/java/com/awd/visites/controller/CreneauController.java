package com.awd.visites.controller;

import com.awd.visites.dto.CreneauLotRequest;
import com.awd.visites.dto.CreneauRequest;
import com.awd.visites.dto.CreneauResponse;
import com.awd.visites.service.CreneauService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/visites/creneaux")
@RequiredArgsConstructor
public class CreneauController {

    private final CreneauService creneauService;

    @PostMapping
    public ResponseEntity<CreneauResponse> creer(@Valid @RequestBody CreneauRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(creneauService.creer(request));
    }

    @PostMapping("/lot")
    public ResponseEntity<List<CreneauResponse>> creerEnLot(@Valid @RequestBody CreneauLotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(creneauService.creerEnLot(request));
    }

    @GetMapping
    public List<CreneauResponse> lister(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return creneauService.listerDisponibles(date);
    }

    @PutMapping("/{id}")
    public CreneauResponse modifier(@PathVariable Long id, @Valid @RequestBody CreneauRequest request) {
        return creneauService.modifier(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        creneauService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}