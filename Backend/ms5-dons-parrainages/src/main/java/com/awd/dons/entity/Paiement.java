package com.awd.dons.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    /** Renseigné si le paiement concerne un don (sinon null). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "don_id")
    private Don don;

    /** Renseigné si le paiement concerne un parrainage (sinon null). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parrainage_id")
    private Parrainage parrainage;
}