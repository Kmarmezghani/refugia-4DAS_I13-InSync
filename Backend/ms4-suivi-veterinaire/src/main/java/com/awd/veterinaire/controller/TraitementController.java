package com.awd.veterinaire.controller;

import com.awd.veterinaire.dto.ChangementStatutTraitementRequest;
import com.awd.veterinaire.dto.TraitementRequest;
import com.awd.veterinaire.dto.TraitementResponse;
import com.awd.veterinaire.service.TraitementService;
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

import java.util.List;

@RestController
@RequestMapping("/api/vet")
@RequiredArgsConstructor
@Tag(name = "Traitements")
public class TraitementController {

    private final TraitementService service;

    @PostMapping("/treatments")
    @ResponseStatus(HttpStatus.CREATED)
    public TraitementResponse creer(@Valid @RequestBody TraitementRequest request) {
        return service.creer(request);
    }

    @GetMapping("/records/{id}/treatments")
    public List<TraitementResponse> lister(@PathVariable Long id) {
        return service.lister(id);
    }

    @PatchMapping("/treatments/{id}/status")
    public TraitementResponse changerStatut(@PathVariable Long id,
            @Valid @RequestBody ChangementStatutTraitementRequest request) {
        return service.changerStatut(id, request.statut());
    }
}
