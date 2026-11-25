package com.awd.visites.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscription")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long evenementId;      // évènement de MS6, simple champ

    @Column(nullable = false)
    private Long participantId;    // utilisateur géré par un autre service

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutInscription statut;

    @Column(nullable = false)
    private LocalDateTime dateInscription;

    @PrePersist
    void avantCreation() {
        if (dateInscription == null) dateInscription = LocalDateTime.now();
        if (statut == null) statut = StatutInscription.INSCRIT;
    }
}