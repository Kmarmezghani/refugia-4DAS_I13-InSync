package com.awd.dons.repository;

import com.awd.dons.entity.Don;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonRepository extends JpaRepository<Don, Long> {

    List<Don> findByDonateurIdOrderByDateDonDesc(Long donateurId);

    boolean existsByDonateurId(Long donateurId);

    @Query("""
            select d from Don d
            where (:animalId is null or d.animalId = :animalId)
              and (:donateurId is null or d.donateur.id = :donateurId)
            order by d.dateDon desc
            """)
    List<Don> rechercher(@Param("animalId") Long animalId,
                         @Param("donateurId") Long donateurId);
}
