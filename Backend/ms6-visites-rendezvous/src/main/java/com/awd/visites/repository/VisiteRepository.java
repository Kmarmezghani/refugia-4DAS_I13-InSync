package com.awd.visites.repository;

import com.awd.visites.entity.Visite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisiteRepository extends JpaRepository<Visite, Long> {

    List<Visite> findByVisiteurId(Long visiteurId);

    List<Visite> findByCreneauId(Long creneauId);

    List<Visite> findByAnimalId(Long animalId);

    List<Visite> findByVisiteurIdAndAnimalId(Long visiteurId, Long animalId);
}