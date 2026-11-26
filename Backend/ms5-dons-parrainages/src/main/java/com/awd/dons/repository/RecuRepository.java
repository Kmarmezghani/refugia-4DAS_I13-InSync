package com.awd.dons.repository;

import com.awd.dons.entity.Recu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecuRepository extends JpaRepository<Recu, Long> {

    Optional<Recu> findByPaiementId(Long paiementId);
}
