package com.awd.animaux.repository;

import com.awd.animaux.entity.Animal;
import com.awd.animaux.entity.StatutAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long>, JpaSpecificationExecutor<Animal> {

    /** Chaque ligne : [StatutAnimal, Long]. */
    @Query("select a.statut, count(a) from Animal a group by a.statut")
    List<Object[]> compterParStatut();

    /** Chaque ligne : [Espece, Long]. */
    @Query("select a.espece, count(a) from Animal a group by a.espece")
    List<Object[]> compterParEspece();

    List<Animal> findByStatut(StatutAnimal statut);
}
