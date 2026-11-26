package com.awd.dons.repository;

import com.awd.dons.entity.Parrainage;
import com.awd.dons.entity.StatutParrainage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ParrainageRepository extends JpaRepository<Parrainage, Long> {

    boolean existsByDonateurId(Long donateurId);

    /** Empêche un donateur de parrainer deux fois le même animal en même temps. */
    boolean existsByDonateurIdAndAnimalIdAndStatutIn(Long donateurId, Long animalId,
                                                     Collection<StatutParrainage> statuts);

    List<Parrainage> findByAnimalIdAndStatutIn(Long animalId, Collection<StatutParrainage> statuts);

    @Query("""
            select p from Parrainage p
            where (:animalId is null or p.animalId = :animalId)
              and (:statut is null or p.statut = :statut)
            order by p.dateDebut desc
            """)
    List<Parrainage> rechercher(@Param("animalId") Long animalId,
                                @Param("statut") StatutParrainage statut);
}
