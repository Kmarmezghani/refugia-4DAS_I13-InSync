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

@Entity
@Table(name = "traitement")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Traitement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dossier_id", nullable = false)
    private DossierMedical dossier;

    @Column(nullable = false, columnDefinition = "text")
    private String medicament;

    @Column(nullable = false, columnDefinition = "text")
    private String posologie;

    @Column(nullable = false)
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTraitement statut;

    public Traitement(DossierMedical dossier, String medicament, String posologie,
            LocalDate dateDebut, LocalDate dateFin) {
        this.dossier = dossier;
        this.medicament = medicament;
        this.posologie = posologie;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        statut = StatutTraitement.EN_COURS;
    }

    public void changerStatut(StatutTraitement cible) {
        statut = cible;
    }
}
