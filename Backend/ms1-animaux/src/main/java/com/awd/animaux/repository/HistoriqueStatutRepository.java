package com.awd.animaux.repository;

import com.awd.animaux.entity.HistoriqueStatut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueStatutRepository extends JpaRepository<HistoriqueStatut, Long> {

    List<HistoriqueStatut> findByAnimalIdOrderByDateChangementAscIdAsc(Long animalId);
}
