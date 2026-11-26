package com.awd.adoptions.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "demande_adoption")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DemandeAdoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // simples références (pas de clé étrangère vers MS1)
    @Column(nullable = false)
    private Long animalId;

    @Column(nullable = false)
    private String clientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDemande statut;

    private String motifRefus;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDecision;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "score_id")
    private ScoreCompatibilite score;

    @PrePersist
    void onCreate() {
        if (statut == null) statut = StatutDemande.EN_ATTENTE;
        dateCreation = LocalDateTime.now();
    }
}