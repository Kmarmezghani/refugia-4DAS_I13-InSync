package com.awd.adoptions.repository;

import com.awd.adoptions.entity.DemandeAdoption;
import com.awd.adoptions.entity.StatutDemande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface DemandeAdoptionRepository extends JpaRepository<DemandeAdoption, Long> {
    List<DemandeAdoption> findByClientId(String clientId);
    boolean existsByAnimalIdAndClientIdAndStatutIn(Long animalId, String clientId,
                                                   Collection<StatutDemande> statuts);
}