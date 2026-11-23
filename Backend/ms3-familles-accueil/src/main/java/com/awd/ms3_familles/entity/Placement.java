package com.awd.ms3_familles.entity;

import com.awd.ms3_familles.enums.IssuePlacement;
import com.awd.ms3_familles.enums.StatutPlacement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Placement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to MS1 (Animals), NO foreign key
    @Column(nullable = false)
    private Long animalId;

    // The family hosting the animal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "famille_id", nullable = false)
    private FamilleAccueil famille;

    @Column(nullable = false)
    private LocalDate dateDebut;

    @Column(nullable = false)
    private LocalDate dateFinPrevue;

    private LocalDate dateFinReelle;

    private String notes;

    @Enumerated(EnumType.STRING)
    private IssuePlacement issue; // Why did it end? (ADOPTION, RETOUR_AU_REFUGE, etc.)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPlacement statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutPlacement.EN_COURS;
        }
    }
}