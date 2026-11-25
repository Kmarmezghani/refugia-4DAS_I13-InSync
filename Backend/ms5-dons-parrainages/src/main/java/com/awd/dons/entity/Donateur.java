package com.awd.dons.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity                      // Cette classe est une table de la base
@Table(name = "donateur")    // Nom exact de la table (comme dans le dossier technique)
@Getter                      // Lombok : génère getNom(), getEmail()...
@Setter                      // Lombok : génère setNom(), setEmail()...
@NoArgsConstructor           // Constructeur vide, obligatoire pour JPA
public class Donateur {

    @Id                                                   // Clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // Auto-incrémentée par PostgreSQL (1, 2, 3...)
    private Long id;

    @Column(nullable = false)                 // NOT NULL
    private String nom;

    @Column(nullable = false, unique = true)  // NOT NULL + UNIQUE : deux donateurs ne peuvent pas avoir le même e-mail
    private String email;

    private String telephone;                 // Facultatif : pas de @Column, donc peut être NULL

    @Column(nullable = false)                 // Devient la colonne "date_inscription"
    private LocalDate dateInscription;
}
