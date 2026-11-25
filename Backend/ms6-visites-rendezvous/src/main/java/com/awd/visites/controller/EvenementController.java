package com.awd.visites.controller;

import com.awd.visites.dto.EvenementRequest;
import com.awd.visites.dto.EvenementResponse;
import com.awd.visites.service.EvenementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evenements")
@RequiredArgsConstructor
public class EvenementController {

    private final EvenementService evenementService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvenementResponse creer(@Valid @RequestBody EvenementRequest request) {
        return evenementService.creer(request);
    }

    @GetMapping
    public List<EvenementResponse> lister(@RequestParam(defaultValue = "false") boolean aVenir) {
        return evenementService.lister(aVenir);
    }

    @PatchMapping("/{id}/annuler")
    public EvenementResponse annuler(@PathVariable Long id) {
        return evenementService.annuler(id);
    }
}