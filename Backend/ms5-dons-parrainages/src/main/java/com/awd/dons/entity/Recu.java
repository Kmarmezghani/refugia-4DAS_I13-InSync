package com.awd.dons.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recu")
@Getter
@Setter
@NoArgsConstructor
public class Recu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "paiement_id", nullable = false, unique = true)
    private Paiement paiement;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private LocalDateTime dateEmission;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montant;
}
