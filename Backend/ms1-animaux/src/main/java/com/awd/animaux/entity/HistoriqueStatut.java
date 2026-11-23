package com.awd.animaux.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** Une ligne par changement de statut : qui a changé quoi, quand et pourquoi. */
@Entity
@Table(name = "historique_statut")
public class HistoriqueStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    /** Vide pour la ligne de création. */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatutAnimal ancienStatut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutAnimal nouveauStatut;

    @Column(nullable = false)
    private LocalDateTime dateChangement;

    /** Qui a demandé le changement : CREATION, PERSONNEL, MS2, MS3... */
    @Column(nullable = false, length = 30)
    private String origine;

    @Column(length = 255)
    private String motif;

    protected HistoriqueStatut() {
        // pour JPA
    }

    HistoriqueStatut(Animal animal, StatutAnimal ancienStatut, StatutAnimal nouveauStatut, String origine, String motif) {
        this.animal = animal;
        this.ancienStatut = ancienStatut;
        this.nouveauStatut = nouveauStatut;
        this.origine = origine;
        this.motif = motif;
        this.dateChangement = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Animal getAnimal() { return animal; }
    public StatutAnimal getAncienStatut() { return ancienStatut; }
    public StatutAnimal getNouveauStatut() { return nouveauStatut; }
    public LocalDateTime getDateChangement() { return dateChangement; }
    public String getOrigine() { return origine; }
    public String getMotif() { return motif; }
}
