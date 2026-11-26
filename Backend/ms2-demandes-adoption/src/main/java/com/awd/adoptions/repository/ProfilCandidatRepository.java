package com.awd.adoptions.repository;

import com.awd.adoptions.entity.ProfilCandidat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProfilCandidatRepository extends JpaRepository<ProfilCandidat, Long> {
    Optional<ProfilCandidat> findByClientId(String clientId);
    boolean existsByClientId(String clientId);
}