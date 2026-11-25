package com.awd.visites.repository;

import com.awd.visites.entity.Creneau;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CreneauRepository extends JpaRepository<Creneau, Long> {

    List<Creneau> findByDateAndPlacesRestantesGreaterThanOrderByHeureDebut(LocalDate date, int places);
    List<Creneau> findByDateGreaterThanEqualAndPlacesRestantesGreaterThanOrderByDateAscHeureDebutAsc(LocalDate date, int places);
}