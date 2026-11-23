package com.awd.ms3_familles.repository;

import com.awd.ms3_familles.entity.Placement;
import com.awd.ms3_familles.enums.StatutPlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlacementRepository extends JpaRepository<Placement, Long> {

    // Find all placements for a specific family
    List<Placement> findByFamilleId(Long familleId);

    // Find all placements for a specific animal (useful to check if it's already placed)
    Optional<Placement> findByAnimalIdAndStatut(Long animalId, StatutPlacement statut);
}