package com.awd.visites.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "evenement")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeEvenement type;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String lieu;

    @Column(nullable = false)
    private int capacite;

    @Column(nullable = false)
    private int placesRestantes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEvenement statut;

    @PrePersist
    void avantCreation() {
        if (statut == null) statut = StatutEvenement.PLANIFIE;
    }
}