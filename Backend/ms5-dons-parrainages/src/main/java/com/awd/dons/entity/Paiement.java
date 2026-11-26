package com.awd.dons.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Table "paiement" : l'argent effectivement versé.
 * Un paiement concerne SOIT un don, SOIT un mois de parrainage
 * (l'une des deux colonnes don_id / parrainage_id est vide).
 * Pas de paiement en ligne réel : on enregistre seulement le mode.
 */
@Entity
@Table(name = "paiement")
@Getter
@Setter
@NoArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "don_id")
    private Don don;

    @ManyToOne
    @JoinColumn(name = "parrainage_id")
    private Parrainage parrainage;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false)
    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModePaiement modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPaiement statut;
}
