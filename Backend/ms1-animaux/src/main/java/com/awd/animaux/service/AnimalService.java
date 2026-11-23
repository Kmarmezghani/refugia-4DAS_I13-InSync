package com.awd.animaux.service;

import com.awd.animaux.common.exception.ConflitMetierException;
import com.awd.animaux.common.exception.RessourceIntrouvableException;
import com.awd.animaux.dto.*;
import com.awd.animaux.entity.*;
import com.awd.animaux.event.Evenements;
import com.awd.animaux.mapper.AnimalMapper;
import com.awd.animaux.repository.AnimalRepository;
import com.awd.animaux.repository.AnimalSpecifications;
import com.awd.animaux.repository.HistoriqueStatutRepository;
import com.awd.animaux.repository.PhotoRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

@Service
@Transactional
public class AnimalService {

    public static final int TAILLE_PAGE_MAX = 50;

    private final AnimalRepository animalRepository;
    private final PhotoRepository photoRepository;
    private final HistoriqueStatutRepository historiqueRepository;
    private final ApplicationEventPublisher events;

    public AnimalService(AnimalRepository animalRepository, PhotoRepository photoRepository,
                         HistoriqueStatutRepository historiqueRepository, ApplicationEventPublisher events) {
        this.animalRepository = animalRepository;
        this.photoRepository = photoRepository;
        this.historiqueRepository = historiqueRepository;
        this.events = events;
    }

    // ---------- lecture ----------

    @Transactional(readOnly = true)
    public PageResponse<AnimalResume> lister(Espece espece, StatutAnimal statut, Taille taille, String q,
                                             int page, int size) {
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), TAILLE_PAGE_MAX),
                Sort.by("nom").ascending().and(Sort.by("id")));
        return PageResponse.of(animalRepository
                .findAll(AnimalSpecifications.filtrer(espece, statut, taille, q), pageable)
                .map(AnimalMapper::toResume));
    }

    @Transactional(readOnly = true)
    public AnimalResponse detail(Long id) {
        return AnimalMapper.toResponse(trouver(id));
    }

    @Transactional(readOnly = true)
    public List<HistoriqueResponse> historique(Long id) {
        trouver(id);
        return historiqueRepository.findByAnimalIdOrderByDateChangementAscIdAsc(id).stream()
                .map(AnimalMapper::toResponse)
                .toList();
    }

    // ---------- écriture ----------

    public AnimalResponse creer(AnimalRequest r) {
        Animal animal = Animal.nouveau(r.nom(), r.espece(), r.sexe(), r.taille(), r.dateArrivee());
        appliquer(animal, r);
        animal = animalRepository.save(animal);
        events.publishEvent(Evenements.animalCree(animal));
        return AnimalMapper.toResponse(animal);
    }

    public AnimalResponse modifier(Long id, AnimalRequest r) {
        Animal animal = trouver(id);
        appliquer(animal, r);
        return AnimalMapper.toResponse(animal);
    }

    /** PATCH /statut : vérifie la transition (409 sinon), écrit l'historique, publie StatutAnimalModifié. */
    public AnimalResponse changerStatut(Long id, ChangementStatutRequest r) {
        Animal animal = trouver(id);
        String origine = (r.origine() == null || r.origine().isBlank()) ? "PERSONNEL" : r.origine().trim().toUpperCase();
        HistoriqueStatut ligne = animal.changerStatut(r.nouveauStatut(), origine, r.motif());
        animalRepository.flush(); // écrit la ligne d'historique avant le commit (id disponible)
        events.publishEvent(Evenements.statutModifie(animal, ligne));
        return AnimalMapper.toResponse(animal);
    }

    public void supprimer(Long id) {
        Animal animal = trouver(id);
        if (!animal.estSupprimable()) {
            throw new ConflitMetierException(
                    "La fiche a déjà changé de statut : elle ne peut plus être supprimée");
        }
        animalRepository.delete(animal);
    }

    public PhotoResponse ajouterPhoto(Long id, PhotoRequest r) {
        Animal animal = trouver(id);
        Photo photo = animal.ajouterPhoto(r.url().trim(), r.principale());
        animalRepository.flush(); // pour obtenir l'id de la photo
        return AnimalMapper.toResponse(photo);
    }

    public void retirerPhoto(Long id, Long photoId) {
        Animal animal = trouver(id);
        Photo photo = photoRepository.findByIdAndAnimalId(photoId, id)
                .orElseThrow(() -> new RessourceIntrouvableException(
                        "Photo " + photoId + " introuvable pour l'animal " + id));
        animal.retirerPhoto(photo);
    }

    // ---------- statistiques ----------

    @Transactional(readOnly = true)
    public StatistiquesResponse statistiques() {
        Map<StatutAnimal, Long> parStatut = new EnumMap<>(StatutAnimal.class);
        for (StatutAnimal s : StatutAnimal.values()) parStatut.put(s, 0L);
        for (Object[] ligne : animalRepository.compterParStatut()) {
            parStatut.put((StatutAnimal) ligne[0], (Long) ligne[1]);
        }

        Map<Espece, Long> parEspece = new EnumMap<>(Espece.class);
        for (Espece e : Espece.values()) parEspece.put(e, 0L);
        for (Object[] ligne : animalRepository.compterParEspece()) {
            parEspece.put((Espece) ligne[0], (Long) ligne[1]);
        }

        OptionalDouble moyenne = animalRepository.findByStatut(StatutAnimal.ADOPTE).stream()
                .filter(a -> a.dateAdoption() != null)
                .mapToLong(a -> Duration.between(a.getDateArrivee().atStartOfDay(), a.dateAdoption()).toDays())
                .average();

        long total = parStatut.values().stream().mapToLong(Long::longValue).sum();
        Double duree = moyenne.isPresent() ? Math.round(moyenne.getAsDouble() * 10) / 10.0 : null;
        return new StatistiquesResponse(total, parStatut, parEspece, duree);
    }

    // ---------- utilitaires ----------

    private Animal trouver(Long id) {
        return animalRepository.findById(id).orElseThrow(() -> RessourceIntrouvableException.animal(id));
    }

    private static void appliquer(Animal a, AnimalRequest r) {
        a.modifier(r.nom().trim(), r.espece(), r.race(), r.sexe(), r.dateNaissance(), r.taille(),
                r.description(), r.niveauEnergie(), r.ententeEnfants(), r.ententeAutresAnimaux(), r.dateArrivee());
    }
}
