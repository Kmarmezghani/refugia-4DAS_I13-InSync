package com.refugia.ms3_familles.entity;

import com.refugia.ms3_familles.enums.StatutFamille;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilleAccueil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String telephone;

    @Column(unique = true, nullable = false)
    private String email;

    private String adresse;

    @Column(nullable = false)
    private Integer capacite;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "famille_especes", joinColumns = @JoinColumn(name = "famille_id"))
    @Column(name = "espece")
    private List<String> especesAcceptees;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFamille statut;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutFamille.ACTIVE;
        }
    }
}