package com.awd.animaux.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false)
    private boolean principale;

    @Column(nullable = false)
    private LocalDateTime dateAjout;

    protected Photo() {
        // pour JPA
    }

    Photo(Animal animal, String url, boolean principale) {
        this.animal = animal;
        this.url = url;
        this.principale = principale;
        this.dateAjout = LocalDateTime.now();
    }

    void setPrincipale(boolean principale) {
        this.principale = principale;
    }

    public Long getId() { return id; }
    public Animal getAnimal() { return animal; }
    public String getUrl() { return url; }
    public boolean isPrincipale() { return principale; }
    public LocalDateTime getDateAjout() { return dateAjout; }
}
