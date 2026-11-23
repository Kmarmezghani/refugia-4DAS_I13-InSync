package com.awd.animaux.repository;

import com.awd.animaux.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Optional<Photo> findByIdAndAnimalId(Long id, Long animalId);
}
