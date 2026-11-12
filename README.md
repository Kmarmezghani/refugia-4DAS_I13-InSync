# Refugia

Application web de gestion d'un refuge pour animaux, construite en **architecture microservices**.

Projet AWD (MT-41) · ESPRIT · Classe **4DAS_I13** · Groupe **InSync 3**

## 1. Idée et problématique

Dans un refuge, les informations sont souvent dispersées (fiches papier, tableurs, messages) : on perd le suivi des animaux, des demandes d'adoption, des soins et des dons.

**Refugia** centralise tout dans une seule application : fiches des animaux, adoptions, familles d'accueil, suivi vétérinaire, dons et parrainages, visites et événements, notifications.

## 2. Acteurs

| Rôle | Qui | Ce qu'il fait |
|---|---|---|
| `CLIENT` | Visiteur, adoptant, donateur, parrain, famille d'accueil | Consulte les animaux, réserve une visite, demande une adoption, fait un don |
| `PERSONNEL` | Employés du refuge et vétérinaire | Gère les animaux, les demandes, les créneaux, les événements, le suivi |
| `ADMIN` | Responsable du refuge | Tout ce que fait le personnel, plus : annuler un événement, gérer les comptes et les rôles |

## 3. Fonctionnalités principales

- Gestion des animaux : fiches, statuts, historique.
- Demandes d'adoption et leur suivi.
- Familles d'accueil et placements.
- Suivi vétérinaire : dossiers médicaux, soins, vaccins.
- Dons, parrainages et statistiques de dons.
- Visites : créneaux, réservation, annulation.
- Événements : inscriptions et liste d'attente.
- Notifications envoyées aux utilisateurs.

## 4. Architecture globale

- **API Gateway** (Spring Cloud Gateway) : point d'entrée unique, le frontend ne parle qu'à lui.
- **Eureka** : découverte des services.
- **Keycloak** : authentification (OIDC) et autorisation par jetons JWT, avec les 3 rôles ci-dessus.
- **Une base de données par microservice** : aucun service ne lit la base d'un autre. Les services se référencent seulement par identifiant (par exemple `animalId`).
- **Communication synchrone** : OpenFeign (REST), pour une information nécessaire tout de suite.
- **Communication asynchrone** : RabbitMQ, pour les événements métier.

Le diagramme d'architecture globale est dans `Documentation/Diagrams/`.

## 5. Microservices

| Service | Rôle | Base | Responsable |
|---|---|---|---|
| `ms1-animaux` | Fiches, statuts et historique des animaux | MySQL | À compléter |
| `ms2-demandes-adoption` | Demandes d'adoption et leur suivi | MySQL | À compléter |
| `ms3-familles-accueil` | Familles d'accueil et placements | MySQL | À compléter |
| `ms4-suivi-veterinaire` | Dossiers médicaux, soins, vaccins | MySQL | À compléter |
| `ms5-dons-parrainages` | Dons, parrainages et statistiques de dons | MySQL | À compléter |
| `ms6-visites-rendezvous` | Créneaux de visite, événements, liste d'attente | MySQL | Kmar |
| `ms7-notifications` | Notifications (service commun, développé par toute l'équipe) | MongoDB | Toute l'équipe |

## 6. Technologies

Spring Boot · Spring Cloud Gateway · Eureka · OpenFeign · RabbitMQ · Keycloak · MySQL · MongoDB (notifications) · Swagger / OpenAPI · Docker Compose · React · Git / GitHub.

## 7. Organisation du dépôt

```
Backend/          un dossier par microservice
Frontend/         application React
Documentation/
  Presentation/   présentation de validation
  Diagrams/       architecture globale et diagrammes des services
README.md
```

## 8. Travail en équipe

- Branche `main` : version stable, on n'y pousse pas directement.
- Une branche par fonctionnalité (exemple : `feature/ms6-creneaux`).
- Chacun travaille dans le dossier de son service.
- Fusion dans `main` par Pull Request relue par un autre membre.
- **Outil de gestion du projet : GitHub Projects**, colonnes `À faire` · `En cours` · `En revue` · `Terminé`, une issue par tâche.

## 9. Planning prévisionnel

| Semaine | Objectif |
|---|---|
| S4 | Conception : diagrammes, contrats d'API, validation intermédiaire |
| S5 | CRUD de chaque service, Gateway, Eureka, Swagger |
| S6 | Communication synchrone (OpenFeign) |
| S7 | Communication asynchrone (RabbitMQ) |
| S8 | Sécurité avec Keycloak |
| S9 | Docker Compose et frontend React |

## 10. Risques

- Contrats d'API qui divergent entre services : on les valide ensemble avant de coder.
- Dépendance entre services pendant le développement : chaque service doit pouvoir démarrer seul.
- Retard sur un service : visible tout de suite sur le tableau GitHub Projects.

## 11. Équipe

Groupe **InSync 3** · classe **4DAS_I13** · 6 membres.
