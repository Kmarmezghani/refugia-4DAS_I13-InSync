# Refugia

Application web de gestion d'un refuge pour animaux, construite en architecture microservices.

Projet AWD (MT-41) · ESPRIT · Classe 4DAS_I13 · Groupe InSync 3

**Technologies :** Spring Boot · Spring Cloud Gateway · Eureka · OpenFeign · RabbitMQ · Keycloak · MySQL · MongoDB · Swagger / OpenAPI · Docker Compose · React · Git / GitHub

## 1. Idée générale et problématique

Dans un refuge, les informations sont souvent dispersées (fiches papier, tableurs, messages) : on perd le suivi des animaux, des demandes d'adoption, des soins et des dons.

Refugia centralise tout dans une seule application pour gérer les animaux, les adoptions, les familles d'accueil, le suivi vétérinaire, les dons, les visites et les événements.

## 2. Acteurs

| Rôle | Qui | Ce qu'il fait |
|---|---|---|
| `CLIENT` | Visiteur, adoptant, donateur, parrain, famille d'accueil | Consulte les animaux, réserve une visite, demande une adoption, fait un don |
| `PERSONNEL` | Employés du refuge et vétérinaire | Gère les animaux, les demandes, les créneaux, les événements, le suivi |
| `ADMIN` | Responsable du refuge | Tout ce que fait le personnel, plus la gestion des comptes et des rôles |

Le diagramme des cas d'utilisation général est dans `Documentation/Diagrams/`.

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

- **API Gateway** : point d'entrée unique de l'application.
- **Eureka** : découverte des services.
- **Sécurité** : Keycloak (OIDC), jetons JWT, rôles `CLIENT`, `PERSONNEL`, `ADMIN`.
- **Données** : une base par microservice, aucun service ne lit la base d'un autre.
- **Communications** : synchrone avec OpenFeign (REST), asynchrone avec RabbitMQ (événements).

Le diagramme d'architecture globale est dans `Documentation/Diagrams/`.

## 5. Microservices et bases de données

| Service | Responsabilité | Base |
|---|---|---|
| `ms1-animaux` | Fiches, statuts et historique des animaux | MySQL |
| `ms2-demandes-adoption` | Demandes d'adoption et leur suivi | MySQL |
| `ms3-familles-accueil` | Familles d'accueil et placements | MySQL |
| `ms4-suivi-veterinaire` | Dossiers médicaux, soins, vaccins | MySQL |
| `ms5-dons-parrainages` | Dons, parrainages et statistiques | MySQL |
| `ms6-visites-rendezvous` | Créneaux de visite, événements, liste d'attente | MySQL |
| `ms7-notifications` | Notifications (service commun) | MongoDB |

## 6. Répartition des tâches

Chaque membre de l'équipe est responsable d'un microservice (conception, API, développement, présentation). Le microservice commun `ms7-notifications` est développé par toute l'équipe. Le dossier technique, la présentation, le frontend et l'infrastructure (Gateway, Eureka, Keycloak, Docker) sont répartis équitablement entre les membres.

## 7. Organisation du dépôt Git et gestion du projet

```
Backend/          un dossier par microservice
Frontend/         application React
Documentation/
  Presentation/   présentation de la séance 4
  Diagrams/       diagrammes d'architecture et des services
README.md
```

- Branche `main` : version stable.
- Une branche par fonctionnalité (exemple : `feature/ms6-creneaux`), fusionnée par Pull Request.
- Outil de gestion du projet : **GitHub Projects** (colonnes À faire, En cours, En revue, Terminé).

## 8. Planning prévisionnel

| Semaine | Objectif |
|---|---|
| S4 | Conception et validation intermédiaire |
| S5 | CRUD de chaque service, Gateway, Eureka, Swagger |
| S6 | Communication synchrone (OpenFeign) |
| S7 | Communication asynchrone (RabbitMQ) |
| S8 | Sécurité avec Keycloak |
| S9 | Docker Compose et frontend React |

## 9. Principaux risques

- Contrats d'API qui divergent entre services : on les valide ensemble avant de coder.
- Dépendance entre services pendant le développement : chaque service doit pouvoir démarrer seul.
- Retard sur un service : visible sur le tableau GitHub Projects.
