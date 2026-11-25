package com.awd.visites.service;

import com.awd.visites.common.exception.BadRequestException;
import com.awd.visites.common.exception.ConflictException;
import com.awd.visites.common.exception.ResourceNotFoundException;
import com.awd.visites.dto.CreneauLotRequest;
import com.awd.visites.dto.CreneauRequest;
import com.awd.visites.dto.CreneauResponse;
import com.awd.visites.entity.Creneau;
import com.awd.visites.mapper.CreneauMapper;
import com.awd.visites.repository.CreneauRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreneauService {

    private final CreneauRepository creneauRepository;
    private final CreneauMapper mapper;

    @Transactional
    public CreneauResponse creer(CreneauRequest request) {
        verifierHeures(request.heureDebut(), request.heureFin());
        return mapper.toResponse(creneauRepository.save(mapper.toEntity(request)));
    }

    @Transactional
    public List<CreneauResponse> creerEnLot(CreneauLotRequest request) {
        verifierHeures(request.heureDebut(), request.heureFin());
        int debutMin = request.heureDebut().toSecondOfDay() / 60;
        int finMin = request.heureFin().toSecondOfDay() / 60;
        int duree = request.dureeMinutes();

        List<Creneau> creneaux = new ArrayList<>();
        for (int m = debutMin; m + duree <= finMin; m += duree) {
            creneaux.add(Creneau.builder()
                    .date(request.date())
                    .heureDebut(LocalTime.of(m / 60, m % 60))
                    .heureFin(LocalTime.of((m + duree) / 60, (m + duree) % 60))
                    .capacite(request.capacite())
                    .placesRestantes(request.capacite())
                    .build());
        }
        if (creneaux.isEmpty()) {
            throw new BadRequestException("La durée est trop longue pour la plage horaire donnée");
        }
        return creneauRepository.saveAll(creneaux).stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<CreneauResponse> listerDisponibles(LocalDate date) {
        List<Creneau> creneaux = (date != null)
                ? creneauRepository.findByDateAndPlacesRestantesGreaterThanOrderByHeureDebut(date, 0)
                : creneauRepository.findByDateGreaterThanEqualAndPlacesRestantesGreaterThanOrderByDateAscHeureDebutAsc(LocalDate.now(), 0);
        return creneaux.stream().map(mapper::toResponse).toList();
    }

    @Transactional
    public CreneauResponse modifier(Long id, CreneauRequest request) {
        verifierHeures(request.heureDebut(), request.heureFin());
        Creneau creneau = trouver(id);

        int reservees = creneau.getCapacite() - creneau.getPlacesRestantes();
        if (request.capacite() < reservees) {
            throw new ConflictException("La capacité ne peut pas être inférieure aux places déjà réservées (" + reservees + ")");
        }
        creneau.setDate(request.date());
        creneau.setHeureDebut(request.heureDebut());
        creneau.setHeureFin(request.heureFin());
        creneau.setCapacite(request.capacite());
        creneau.setPlacesRestantes(request.capacite() - reservees);
        return mapper.toResponse(creneauRepository.save(creneau));
    }

    @Transactional
    public void supprimer(Long id) {
        creneauRepository.delete(trouver(id));
    }

    private Creneau trouver(Long id) {
        return creneauRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Créneau introuvable : " + id));
    }

    private void verifierHeures(LocalTime debut, LocalTime fin) {
        if (!fin.isAfter(debut)) {
            throw new BadRequestException("L'heure de fin doit être après l'heure de début");
        }
    }
}