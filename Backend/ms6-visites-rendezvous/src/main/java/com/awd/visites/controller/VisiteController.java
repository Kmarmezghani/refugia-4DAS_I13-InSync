package com.awd.visites.controller;

import com.awd.visites.dto.VisiteRequest;
import com.awd.visites.dto.VisiteResponse;
import com.awd.visites.service.VisiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visites")
@RequiredArgsConstructor
public class VisiteController {

    private final VisiteService visiteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisiteResponse creer(@Valid @RequestBody VisiteRequest request) {
        return visiteService.creer(request);
    }

    @GetMapping
    public List<VisiteResponse> lister(@RequestParam(required = false) Long visiteurId,
                                       @RequestParam(required = false) Long animalId) {
        return visiteService.lister(visiteurId, animalId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void annuler(@PathVariable Long id,
                        @RequestParam(required = false) String motif) {
        visiteService.annuler(id, motif);
    }

    @PatchMapping("/{id}/effectuee")
    public VisiteResponse marquerEffectuee(@PathVariable Long id) {
        return visiteService.marquerEffectuee(id);
    }
}