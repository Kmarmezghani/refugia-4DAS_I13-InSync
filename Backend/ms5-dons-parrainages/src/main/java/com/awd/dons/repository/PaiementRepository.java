package com.awd.dons.repository;

import com.awd.dons.entity.Paiement;
import com.awd.dons.entity.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    boolean existsByParrainageIdAndStatutAndDatePaiementBetween(
            Long parrainageId, StatutPaiement statut, LocalDateTime debut, LocalDateTime fin);

    List<Paiement> findByParrainageIdOrderByDatePaiementDesc(Long parrainageId);

    /** Le paiement le plus récent d'un don. */
    Optional<Paiement> findFirstByDonIdOrderByDatePaiementDesc(Long donId);

    /** Utilisé par StatsService pour les statistiques sur une période. */
    List<Paiement> findByStatutAndDatePaiementBetween(
            StatutPaiement statut, LocalDateTime debut, LocalDateTime fin);
}