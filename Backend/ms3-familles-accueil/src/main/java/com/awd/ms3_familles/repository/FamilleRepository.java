package com.awd.ms3_familles.repository;

import com.awd.ms3_familles.entity.FamilleAccueil;
import com.awd.ms3_familles.enums.StatutFamille;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilleRepository extends JpaRepository<FamilleAccueil, Long> {
    List<FamilleAccueil> findByStatut(StatutFamille statut);

    @Query("SELECT f FROM FamilleAccueil f JOIN f.especesAcceptees e WHERE e = :espece")
    List<FamilleAccueil> findByEspece(@Param("espece") String espece);
}