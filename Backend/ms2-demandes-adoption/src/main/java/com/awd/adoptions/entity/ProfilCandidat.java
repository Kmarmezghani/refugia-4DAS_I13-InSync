package com.awd.adoptions.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profil_candidat")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProfilCandidat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String clientId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeLogement typeLogement;

    private boolean jardin;
    private int nbPersonnes;
    private boolean enfants;
    private boolean autresAnimaux;
    private String experienceAnimaux;
    private String disponibilites;
}