package com.awd.visites.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visite")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Visite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long visiteurId;      // utilisateur (géré par un autre service)

    @Column(nullable = false)
    private Long animalId;        // animal (MS1), simple champ, pas de clé étrangère

    private String nomAnimal;     // copie locale du nom
    private String espece;        // copie locale de l'espèce

    @Column(nullable = false)
    private Long creneauId;       // créneau réservé (table creneau de MS6)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVisite statut;

    private String motifAnnulation;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    void avantCreation() {
        if (dateCreation == null) dateCreation = LocalDateTime.now();
        if (statut == null) statut = StatutVisite.CONFIRMEE;
    }
}