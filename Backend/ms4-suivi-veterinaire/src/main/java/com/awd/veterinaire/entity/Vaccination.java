package com.awd.veterinaire.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "vaccination")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vaccination {

    public static final int JOURS_AVANT_RAPPEL = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dossier_id", nullable = false)
    private DossierMedical dossier;

    @Column(nullable = false, columnDefinition = "text")
    private String vaccin;

    @Column(nullable = false)
    private LocalDate dateVaccination;

    private LocalDate dateRappel;

    @Column(nullable = false)
    private boolean rappelPublie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVaccination statut;

    public Vaccination(DossierMedical dossier, String vaccin, LocalDate dateVaccination,
            LocalDate dateRappel, LocalDate aujourdhui) {
        this.dossier = dossier;
        corriger(vaccin, dateVaccination, dateRappel, aujourdhui);
    }

    public void corriger(String vaccin, LocalDate dateVaccination, LocalDate dateRappel, LocalDate aujourdhui) {
        if (!Objects.equals(this.dateRappel, dateRappel)) {
            rappelPublie = false;
        }
        this.vaccin = vaccin;
        this.dateVaccination = dateVaccination;
        this.dateRappel = dateRappel;
        actualiserStatut(aujourdhui);
    }

    public StatutVaccination calculerStatut(LocalDate aujourdhui) {
        if (dateRappel == null || dateRappel.isAfter(aujourdhui.plusDays(JOURS_AVANT_RAPPEL))) {
            return StatutVaccination.A_JOUR;
        }
        if (dateRappel.isBefore(aujourdhui)) {
            return StatutVaccination.EN_RETARD;
        }
        return StatutVaccination.A_RAPPELER;
    }

    public void actualiserStatut(LocalDate aujourdhui) {
        statut = calculerStatut(aujourdhui);
    }

    public void marquerRappelPublie() {
        rappelPublie = true;
    }
}
