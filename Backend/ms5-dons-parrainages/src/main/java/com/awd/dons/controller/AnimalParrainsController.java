package com.awd.dons.controller;

import com.awd.dons.dto.ParrainageResponse;
import com.awd.dons.service.ParrainageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Parrains d'un animal")
public class AnimalParrainsController {

    private final ParrainageService parrainageService;

    @GetMapping("/api/animaux/{animalId}/parrains")
    @Operation(summary = "Lister les parrains actifs d'un animal")
    public List<ParrainageResponse> parrains(@PathVariable Long animalId) {
        return parrainageService.parrainsDeLAnimal(animalId);
    }
}
