package com.awd.veterinaire.service;

import com.awd.veterinaire.entity.StatutVaccination;
import com.awd.veterinaire.entity.Vaccination;
import com.awd.veterinaire.repository.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Clock;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RappelVaccinationService {

    private final VaccinationRepository repository;
    private final ApplicationEventPublisher events;
    private final Environment environnement;
    private final Clock horloge;

    @Scheduled(cron = "${ms4.rappels.cron:0 0 8 * * *}")
    @Transactional
    public void verifierRappels() {
        LocalDate aujourdhui = LocalDate.now(horloge);
        LocalDate limite = aujourdhui.plusDays(Vaccination.JOURS_AVANT_RAPPEL);
        boolean simulationActive = environnement.matchesProfiles("dev");
        for (Vaccination vaccination : repository.verrouillerRappels(limite, StatutVaccination.EN_RETARD)) {
            vaccination.actualiserStatut(aujourdhui);
            if (simulationActive && vaccination.getStatut() == StatutVaccination.A_RAPPELER
                    && !vaccination.isRappelPublie()) {
                vaccination.marquerRappelPublie();
                events.publishEvent(new RappelVaccination(vaccination.getDossier().getAnimalId(),
                        vaccination.getVaccin(), vaccination.getDateRappel()));
            }
        }
    }

    @TransactionalEventListener
    public void journaliserRappelSimule(RappelVaccination rappel) {
        log.info("RappelVaccination simulé pour animalId={}, vaccin={}, dateRappel={}",
                rappel.animalId(), rappel.vaccin(), rappel.dateRappel());
    }
}
