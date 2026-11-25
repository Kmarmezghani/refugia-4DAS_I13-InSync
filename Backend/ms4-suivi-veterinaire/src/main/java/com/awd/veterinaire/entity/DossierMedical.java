package com.awd.veterinaire.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "dossier_medical")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DossierMedical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private Long animalId;

    @Column(nullable = false, updatable = false)
    private String numeroDossier;

    @Column(nullable = false, updatable = false)
    private LocalDate dateOuverture;

    @Column(columnDefinition = "text")
    private String notesGenerales;

    public DossierMedical(Long animalId, String numeroDossier, LocalDate dateOuverture) {
        this.animalId = animalId;
        this.numeroDossier = numeroDossier;
        this.dateOuverture = dateOuverture;
    }

    public void modifierNotesGenerales(String notesGenerales) {
        this.notesGenerales = notesGenerales;
    }
}
