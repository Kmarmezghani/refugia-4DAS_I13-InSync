package com.awd.animaux.controller;

import com.awd.animaux.dto.*;
import com.awd.animaux.entity.Espece;
import com.awd.animaux.entity.StatutAnimal;
import com.awd.animaux.entity.Taille;
import com.awd.animaux.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Les 10 endpoints de MS1 (contrat d'API, section 7.1).
 * Les rôles (PERSONNEL, ADMIN) seront vérifiés à l'étape Keycloak.
 */
@RestController
@RequestMapping("/api/animaux")
@Tag(name = "Animaux", description = "Fiches, statuts, photos et historique")
public class AnimalController {

    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lister et filtrer les animaux (sans filtre de statut, les adoptés sont exclus)")
    public PageResponse<AnimalResume> lister(@RequestParam(required = false) Espece espece,
                                             @RequestParam(required = false) StatutAnimal statut,
                                             @RequestParam(required = false) Taille taille,
                                             @RequestParam(required = false) String q,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "12") int size) {
        return service.lister(espece, statut, taille, q, page, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détail d'une fiche avec ses photos (appelé aussi par MS2 à MS6)")
    public AnimalResponse detail(@PathVariable Long id) {
        return service.detail(id);
    }

    @PostMapping
    @Operation(summary = "Créer une fiche (statut DISPONIBLE, publie AnimalCréé)")
    public ResponseEntity<AnimalResponse> creer(@Valid @RequestBody AnimalRequest request) {
        AnimalResponse cree = service.creer(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cree.id()).toUri();
        return ResponseEntity.created(location).body(cree);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une fiche, hors statut")
    public AnimalResponse modifier(@PathVariable Long id, @Valid @RequestBody AnimalRequest request) {
        return service.modifier(id, request);
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Changer le statut (409 si transition interdite, publie StatutAnimalModifié)")
    public AnimalResponse changerStatut(@PathVariable Long id, @Valid @RequestBody ChangementStatutRequest request) {
        return service.changerStatut(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer une fiche saisie par erreur (409 si elle a déjà changé de statut)")
    public void supprimer(@PathVariable Long id) {
        service.supprimer(id);
    }

    @GetMapping("/{id}/historique")
    @Operation(summary = "Parcours des statuts de l'animal")
    public List<HistoriqueResponse> historique(@PathVariable Long id) {
        return service.historique(id);
    }

    @PostMapping("/{id}/photos")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter une photo (5 au maximum, 409 au-delà)")
    public PhotoResponse ajouterPhoto(@PathVariable Long id, @Valid @RequestBody PhotoRequest request) {
        return service.ajouterPhoto(id, request);
    }

    @DeleteMapping("/{id}/photos/{photoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Retirer une photo")
    public void retirerPhoto(@PathVariable Long id, @PathVariable Long photoId) {
        service.retirerPhoto(id, photoId);
    }

    @GetMapping("/statistiques")
    @Operation(summary = "Nombre d'animaux par statut et par espèce, durée moyenne de séjour")
    public StatistiquesResponse statistiques() {
        return service.statistiques();
    }
}
