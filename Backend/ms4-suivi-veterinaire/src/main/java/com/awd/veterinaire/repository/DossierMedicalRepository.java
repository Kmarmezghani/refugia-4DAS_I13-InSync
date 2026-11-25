package com.awd.veterinaire.repository;

import com.awd.veterinaire.entity.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Long> {

    Optional<DossierMedical> findByAnimalId(Long animalId);

    boolean existsByAnimalId(Long animalId);
}
