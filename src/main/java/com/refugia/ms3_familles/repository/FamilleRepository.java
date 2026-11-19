package com.refugia.ms3_familles.repository;

import com.refugia.ms3_familles.entity.FamilleAccueil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FamilleRepository extends JpaRepository<FamilleAccueil, Long> {
}