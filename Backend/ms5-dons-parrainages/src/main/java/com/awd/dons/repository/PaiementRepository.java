package com.awd.dons.repository;

import com.awd.dons.entity.Paiement;
import com.awd.dons.entity.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    Optional<Paiement> findFirstByDonId(Long donId);

    List<Paiement> findByParrainageIdOrderByDatePaiementDesc(Long parrainageId);

    boolean existsByParrainageIdAndStatutAndDatePaiementBetween(Long parrainageId, StatutPaiement statut,
                                                               LocalDateTime debut, LocalDateTime fin);

    List<Paiement> findByStatutAndDatePaiementBetween(StatutPaiement statut,
                                                      LocalDateTime debut, LocalDateTime fin);
}
