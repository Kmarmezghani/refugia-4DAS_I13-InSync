package com.awd.dons.repository;
import com.awd.dons.entity.Donateur;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DonateurRepository extends JpaRepository<Donateur, Long> {

    boolean existsByEmailIgnoreCase(String email);
}