package com.awd.dons.controller;

import com.awd.dons.dto.DonateurRequest;
import com.awd.dons.dto.DonateurResponse;
import com.awd.dons.service.DonateurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                      // Renvoie du JSON
@RequestMapping("/api/donateurs")    // Préfixe commun à toutes les routes de cette classe
@RequiredArgsConstructor
@Tag(name = "Donateurs")             // Titre du groupe dans Swagger
public class DonateurController {

    private final DonateurService donateurService;

    @PostMapping
    @Operation(summary = "Créer un donateur")
    public ResponseEntity<DonateurResponse> creer(@Valid @RequestBody DonateurRequest req) {
        // @Valid déclenche les vérifications (@NotBlank, @Email...) -> 400 si invalide
        // 201 Created au lieu de 200
        return ResponseEntity.status(HttpStatus.CREATED).body(donateurService.creer(req));
    }

    @GetMapping
    @Operation(summary = "Lister les donateurs")
    public List<DonateurResponse> lister() {
        return donateurService.lister();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'un donateur")
    public DonateurResponse getById(@PathVariable Long id) {   // {id} de l'URL -> paramètre id
        return donateurService.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un donateur")
    public DonateurResponse modifier(@PathVariable Long id, @Valid @RequestBody DonateurRequest req) {
        return donateurService.modifier(id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un donateur")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        donateurService.supprimer(id);
        return ResponseEntity.noContent().build();   // 204 No Content
    }
}