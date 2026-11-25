package com.awd.visites.repository;

import com.awd.visites.entity.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    List<Evenement> findByDateGreaterThanEqualOrderByDateAsc(LocalDateTime date);

    List<Evenement> findAllByOrderByDateAsc();
}