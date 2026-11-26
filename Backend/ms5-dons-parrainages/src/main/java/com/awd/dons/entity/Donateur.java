package com.awd.dons.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Table(name = "donateur")
@Getter
@Setter
@NoArgsConstructor
public class Donateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    @Column(nullable = false)
    private LocalDate dateInscription;
}
