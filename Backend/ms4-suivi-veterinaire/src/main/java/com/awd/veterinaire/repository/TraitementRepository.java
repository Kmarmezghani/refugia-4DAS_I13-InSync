package com.awd.veterinaire.repository;

import com.awd.veterinaire.entity.Traitement;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TraitementRepository extends JpaRepository<Traitement, Long> {

    List<Traitement> findAllByDossier_IdOrderByDateDebutDescIdDesc(Long dossierId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Traitement t where t.id = :id")
    Optional<Traitement> verrouillerParId(Long id);
}
