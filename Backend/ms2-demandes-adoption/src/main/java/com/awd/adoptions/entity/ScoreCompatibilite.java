package com.awd.adoptions.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "score_compatibilite")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ScoreCompatibilite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int scoreTotal;
    private LocalDateTime dateCalcul;
}