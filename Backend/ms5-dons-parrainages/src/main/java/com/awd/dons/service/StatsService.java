package com.awd.dons.service;

import com.awd.dons.common.exception.BadRequestException;
import com.awd.dons.dto.StatsDonsResponse;
import com.awd.dons.dto.StatsDonsResponse.AnimalSoutenu;
import com.awd.dons.entity.Paiement;
import com.awd.dons.entity.StatutDon;
import com.awd.dons.entity.StatutPaiement;
import com.awd.dons.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class StatsService {

    private static final int TOP = 5;

    private final PaiementRepository paiementRepository;

    @Transactional(readOnly = true)
    public StatsDonsResponse statistiques(LocalDate debut, LocalDate fin) {
        if (debut.isAfter(fin)) {
            throw new BadRequestException("La date de début doit être avant la date de fin");
        }

        List<Paiement> paiements = paiementRepository
                .findByStatutAndDatePaiementBetween(StatutPaiement.VALIDE, debut.atStartOfDay(), fin.atTime(LocalTime.MAX))
                .stream()
                .filter(p -> p.getDon() == null || p.getDon().getStatut() != StatutDon.ANNULE)
                .toList();

        BigDecimal total = BigDecimal.ZERO;
        long nbDons = 0;
        long nbPaiementsParrainage = 0;
        Map<Long, BigDecimal> parAnimal = new HashMap<>();

        for (Paiement p : paiements) {
            total = total.add(p.getMontant());
            Long animalId;
            if (p.getDon() != null) {
                nbDons++;
                animalId = p.getDon().getAnimalId();
            } else {
                nbPaiementsParrainage++;
                animalId = p.getParrainage().getAnimalId();
            }
            if (animalId != null) {
                parAnimal.merge(animalId, p.getMontant(), BigDecimal::add);
            }
        }

        List<AnimalSoutenu> top = parAnimal.entrySet().stream()
                .sorted(Map.Entry.<Long, BigDecimal>comparingByValue().reversed())
                .limit(TOP)
                .map(e -> new AnimalSoutenu(e.getKey(), e.getValue()))
                .toList();

        return new StatsDonsResponse(debut, fin, total, nbDons, nbPaiementsParrainage, top);
    }
}
