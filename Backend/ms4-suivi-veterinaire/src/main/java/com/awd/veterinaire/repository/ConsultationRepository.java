package com.awd.veterinaire.repository;

import com.awd.veterinaire.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    List<Consultation> findAllByDossier_IdOrderByDateConsultationDescIdDesc(Long dossierId);
}
