package com.awd.ms3_familles.repository;

import com.awd.ms3_familles.entity.Placement;
import com.awd.ms3_familles.enums.StatutPlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlacementRepository extends JpaRepository<Placement, Long> {
    List<Placement> findByFamilleId(Long familleId);
    List<Placement> findByStatut(StatutPlacement statut);
    List<Placement> findByAnimalId(Long animalId);
    Optional<Placement> findByAnimalIdAndStatut(Long animalId, StatutPlacement statut);
}