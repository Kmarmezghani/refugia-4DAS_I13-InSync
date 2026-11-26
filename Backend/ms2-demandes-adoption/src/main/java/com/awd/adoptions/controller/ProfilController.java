package com.awd.adoptions.controller;

import com.awd.adoptions.dto.ProfilRequest;
import com.awd.adoptions.dto.ProfilResponse;
import com.awd.adoptions.dto.ProfilUpdateRequest;
import com.awd.adoptions.service.ProfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profils")
@RequiredArgsConstructor
public class ProfilController {

    private final ProfilService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProfilResponse creer(@Valid @RequestBody ProfilRequest request) {
        return service.creer(request);
    }

    @GetMapping("/{id}")
    public ProfilResponse trouver(@PathVariable Long id) {
        return service.trouver(id);
    }

    @PutMapping("/{id}")
    public ProfilResponse modifier(@PathVariable Long id, @Valid @RequestBody ProfilUpdateRequest request) {
        return service.modifier(id, request);
    }

    @GetMapping("/client/{clientId}")
    public ProfilResponse trouverParClient(@PathVariable String clientId) {
        return service.trouverParClient(clientId);
    }
    @GetMapping
    public List<ProfilResponse> lister() {
        return service.lister();
    }
}