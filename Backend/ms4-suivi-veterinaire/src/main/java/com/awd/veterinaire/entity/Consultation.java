package com.awd.veterinaire.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "consultation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dossier_id", nullable = false)
    private DossierMedical dossier;

    @Column(nullable = false)
    private LocalDate dateConsultation;

    @Column(nullable = false, columnDefinition = "text")
    private String motif;

    @Column(columnDefinition = "text")
    private String diagnostic;

    private Double poids;

    private Double temperature;

    @Column(columnDefinition = "text")
    private String remarques;

    public Consultation(DossierMedical dossier, LocalDate dateConsultation, String motif,
            String diagnostic, Double poids, Double temperature, String remarques) {
        this.dossier = dossier;
        this.dateConsultation = dateConsultation;
        this.motif = motif;
        this.diagnostic = diagnostic;
        this.poids = poids;
        this.temperature = temperature;
        this.remarques = remarques;
    }
}
