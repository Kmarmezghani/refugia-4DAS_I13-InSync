package com.awd.dons.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Table "don" : un don ponctuel.
 * animalId est une simple référence vers MS1 (pas de clé étrangère :
 * la table des animaux est dans une autre base). Il est optionnel :
 * on peut donner au refuge en général.
 */
@Entity
@Table(name = "don")
@Getter
@Setter
@NoArgsConstructor
public class Don {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "donateur_id", nullable = false)
    private Donateur donateur;

    private Long animalId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDon statut;

    @Column(nullable = false)
    private LocalDateTime dateDon;

    private String message;
}
