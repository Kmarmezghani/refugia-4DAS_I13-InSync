package com.awd.dons.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "parrainage")
@Getter
@Setter
@NoArgsConstructor
public class Parrainage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "donateur_id", nullable = false)
    private Donateur donateur;

    @Column(nullable = false)
    private Long animalId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montantMensuel;

    @Column(nullable = false)
    private LocalDate dateDebut;

    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutParrainage statut;
}
