package com.awd.visites.repository;

import com.awd.visites.entity.Inscription;
import com.awd.visites.entity.StatutInscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    List<Inscription> findByEvenementIdOrderByDateInscriptionAsc(Long evenementId);
    boolean existsByEvenementIdAndParticipantIdAndStatut(Long evenementId, Long participantId, StatutInscription statut);
}