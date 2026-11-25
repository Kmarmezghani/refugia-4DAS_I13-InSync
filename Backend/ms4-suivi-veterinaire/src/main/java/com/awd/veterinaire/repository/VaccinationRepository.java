package com.awd.veterinaire.repository;

import com.awd.veterinaire.entity.StatutVaccination;
import com.awd.veterinaire.entity.Vaccination;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

    List<Vaccination> findAllByDossier_IdOrderByDateVaccinationDescIdDesc(Long dossierId);

    List<Vaccination> findAllByDateRappelIsNotNullOrderByDateRappelAscIdAsc();

    List<Vaccination> findAllByDateRappelBeforeOrderByDateRappelAscIdAsc(LocalDate limiteExclusive);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Vaccination v where v.id = :id")
    Optional<Vaccination> verrouillerParId(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select v from Vaccination v
            where v.dateRappel <= :limite and v.statut <> :statutPasse
            order by v.id
            """)
    List<Vaccination> verrouillerRappels(LocalDate limite, StatutVaccination statutPasse);
}
