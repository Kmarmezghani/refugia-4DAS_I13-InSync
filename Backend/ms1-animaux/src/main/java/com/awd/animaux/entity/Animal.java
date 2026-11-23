package com.awd.animaux.entity;

import com.awd.animaux.common.exception.ConflitMetierException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Fiche d'un animal. Animal porte le statut ; ses photos et son historique lui sont rattachés
 * (composition : supprimer l'animal supprime ses photos et son historique).
 */
@Entity
@Table(name = "animal")
public class Animal {

    public static final int MAX_PHOTOS = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Espece espece;

    @Column(length = 60)
    private String race;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Sexe sexe;

    private LocalDate dateNaissance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Taille taille;

    @Column(length = 1000)
    private String description;

    /** De 1 (très calme) à 5 (très actif) : sert au score de compatibilité de MS2. */
    @Column(nullable = false)
    private int niveauEnergie;

    @Column(nullable = false)
    private boolean ententeEnfants;

    @Column(nullable = false)
    private boolean ententeAutresAnimaux;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutAnimal statut;

    @Column(nullable = false)
    private LocalDate dateArrivee;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateAjout ASC, id ASC")
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateChangement ASC, id ASC")
    private List<HistoriqueStatut> historique = new ArrayList<>();

    protected Animal() {
        // pour JPA
    }

    /** Crée une fiche : statut DISPONIBLE et première ligne d'historique. */
    public static Animal nouveau(String nom, Espece espece, Sexe sexe, Taille taille, LocalDate dateArrivee) {
        Animal a = new Animal();
        a.nom = nom;
        a.espece = espece;
        a.sexe = sexe;
        a.taille = taille;
        a.niveauEnergie = 3;
        a.dateArrivee = dateArrivee != null ? dateArrivee : LocalDate.now();
        a.statut = StatutAnimal.DISPONIBLE;
        a.historique.add(new HistoriqueStatut(a, null, StatutAnimal.DISPONIBLE, "CREATION", "Arrivée au refuge"));
        return a;
    }

    // ---------- règles métier ----------

    public boolean transitionAutorisee(StatutAnimal cible) {
        return statut.peutPasserA(cible);
    }

    /**
     * Change le statut si la transition est autorisée et écrit l'historique.
     * @return la ligne d'historique créée
     * @throws ConflitMetierException si la transition est interdite (409)
     */
    public HistoriqueStatut changerStatut(StatutAnimal nouveau, String origine, String motif) {
        if (!transitionAutorisee(nouveau)) {
            throw new ConflitMetierException("Transition interdite : " + statut + " → " + nouveau);
        }
        HistoriqueStatut ligne = new HistoriqueStatut(this, statut, nouveau, origine, motif);
        historique.add(ligne);
        statut = nouveau;
        return ligne;
    }

    /** Le catalogue public n'affiche pas les animaux déjà adoptés. */
    public boolean estVisiblePublic() {
        return statut != StatutAnimal.ADOPTE;
    }

    /** Une fiche ne peut être supprimée que si elle n'a jamais changé de statut (saisie par erreur). */
    public boolean estSupprimable() {
        return historique.size() <= 1;
    }

    public Photo ajouterPhoto(String url, boolean principale) {
        if (photos.size() >= MAX_PHOTOS) {
            throw new ConflitMetierException("Un animal a au maximum " + MAX_PHOTOS + " photos");
        }
        boolean devientPrincipale = principale || photos.isEmpty();
        if (devientPrincipale) {
            photos.forEach(p -> p.setPrincipale(false));
        }
        Photo photo = new Photo(this, url, devientPrincipale);
        photos.add(photo);
        return photo;
    }

    public void retirerPhoto(Photo photo) {
        photos.remove(photo);
        if (photo.isPrincipale() && !photos.isEmpty()) {
            photos.get(0).setPrincipale(true);
        }
    }

    /** Date du passage à ADOPTE, si l'animal a été adopté. */
    public LocalDateTime dateAdoption() {
        return historique.stream()
                .filter(h -> h.getNouveauStatut() == StatutAnimal.ADOPTE)
                .map(HistoriqueStatut::getDateChangement)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

    /** Modification de la fiche, hors statut (PUT). */
    public void modifier(String nom, Espece espece, String race, Sexe sexe, LocalDate dateNaissance, Taille taille,
                         String description, int niveauEnergie, boolean ententeEnfants,
                         boolean ententeAutresAnimaux, LocalDate dateArrivee) {
        this.nom = nom;
        this.espece = espece;
        this.race = race;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.taille = taille;
        this.description = description;
        this.niveauEnergie = niveauEnergie;
        this.ententeEnfants = ententeEnfants;
        this.ententeAutresAnimaux = ententeAutresAnimaux;
        if (dateArrivee != null) {
            this.dateArrivee = dateArrivee;
        }
    }

    // ---------- accesseurs ----------

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public Espece getEspece() { return espece; }
    public String getRace() { return race; }
    public Sexe getSexe() { return sexe; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public Taille getTaille() { return taille; }
    public String getDescription() { return description; }
    public int getNiveauEnergie() { return niveauEnergie; }
    public boolean isEntenteEnfants() { return ententeEnfants; }
    public boolean isEntenteAutresAnimaux() { return ententeAutresAnimaux; }
    public StatutAnimal getStatut() { return statut; }
    public LocalDate getDateArrivee() { return dateArrivee; }
    public List<Photo> getPhotos() { return Collections.unmodifiableList(photos); }
    public List<HistoriqueStatut> getHistorique() { return Collections.unmodifiableList(historique); }
}
