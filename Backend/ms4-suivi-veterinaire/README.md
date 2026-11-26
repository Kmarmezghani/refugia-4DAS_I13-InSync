# MS4 · Suivi vétérinaire

Service Spring Boot 3.5 pour le suivi vétérinaire de Refugia. L'application utilise Java 17 et PostgreSQL 16. Elle écoute sur le port `8040`.

## Tests

Depuis ce dossier, avec `JAVA_HOME` pointant vers un JDK 17, lancer la commande PowerShell suivante.

```powershell
.\mvnw.cmd test
```

Les tests activent le profil `test`, qui utilise une base H2 en mémoire en mode PostgreSQL. Ils vérifient le démarrage, Swagger et les parcours des dossiers médicaux, des consultations, des vaccinations et des traitements.

## Démarrage local

Créer une base PostgreSQL 16 avec un utilisateur autorisé à y créer des tables. MS4 initialise les tables `dossier_medical`, `consultation`, `vaccination` et `traitement` au démarrage. L'URL par défaut est `jdbc:postgresql://localhost:5432/veterinaire_db`. Définir `MS4_DB_URL` si la base utilise une autre URL.

```powershell
$env:MS4_DB_USER = Read-Host 'Utilisateur PostgreSQL'
$motDePasse = Read-Host 'Mot de passe PostgreSQL' -AsSecureString
$env:MS4_DB_PASSWORD = [System.Net.NetworkCredential]::new('', $motDePasse).Password
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

Ouvrir [Swagger UI](http://localhost:8040/swagger-ui.html) ou consulter le document [OpenAPI JSON](http://localhost:8040/v3/api-docs).

## Découverte de service avec Eureka

Le profil `eureka` active l'enregistrement sous le nom `ms4-suivi-veterinaire` et la lecture du registre. Le port de MS4 est `8040`. Le démarrage habituel avec `dev` et les tests H2 fonctionnent avec Eureka désactivé.

Avec PostgreSQL et un registre Eureka disponibles, se placer dans le dossier MS4. Dans le même terminal PowerShell, configurer `JAVA_HOME` pour Java 17 et renseigner `MS4_DB_USER` et `MS4_DB_PASSWORD` comme ci-dessus. Lancer ensuite MS4 avec le profil `eureka`.

```powershell
$env:EUREKA_CLIENT_SERVICEURL_DEFAULTZONE = 'http://localhost:8761/eureka/'
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=eureka"
```

L'URL est facultative pour un registre local sur le port `8761`. Dans le réseau Docker du groupe, utiliser l'URL du registre partagé, par exemple `http://eureka:8761/eureka/`. Vérifier dans le tableau de bord Eureka que `MS4-SUIVI-VETERINAIRE` apparaît avec le statut `UP`.

## Essayer le parcours d'un dossier

Avec le profil local `dev`, MS4 simule la vérification de MS1 pour les animaux `1`, `2` et `12`. Sans ce profil, l'ouverture renvoie `503` tant que le client MS1 n'est pas intégré. Les autorisations Keycloak seront ajoutées lors de l'intégration.

1. Dans `POST /api/vet/records`, envoyer `{"animalId":12}` et relever l'`id` du dossier retourné.
2. Lire le dossier avec `GET /api/vet/records/{id}` ou avec `GET /api/vet/records/by-animal/12`.
3. Dans `PATCH /api/vet/records/{id}`, envoyer `{"notesGenerales":"Suivi à planifier"}`. La valeur `null` efface les notes.

Une seconde ouverture pour le même animal renvoie `409`.

## Enregistrer une consultation

Dans `POST /api/vet/consultations`, utiliser l'`id` d'un dossier existant pour `dossierId`.

```json
{
  "dossierId": 1,
  "dateConsultation": "2026-11-25",
  "motif": "Contrôle de routine"
}
```

Les champs `diagnostic`, `poids`, `temperature` et `remarques` sont facultatifs. Si le poids est renseigné, il doit être positif.

Consulter l'historique avec `GET /api/vet/records/1/consultations`. Il est trié par date décroissante. Pour lire une consultation précise, utiliser son `id` dans `GET /api/vet/consultations/{id}`.

## Enregistrer une vaccination et consulter les rappels

Envoyer ce corps dans `POST /api/vet/vaccinations` avec l'identifiant d'un dossier existant.

```json
{
  "dossierId": 1,
  "vaccin": "Rage",
  "dateVaccination": "2026-11-25",
  "dateRappel": "2026-12-02"
}
```

L'historique est disponible dans `GET /api/vet/records/{id}/vaccinations`. Dans `PATCH /api/vet/vaccinations/{id}`, renseigner les champs à corriger. Envoyer `{"dateRappel":null}` pour retirer le rappel.

`GET /api/vet/vaccinations/reminders` retourne les vaccinations ayant une date de rappel, de la plus ancienne à la plus récente. Le filtre facultatif `beforeDate` retient les rappels strictement antérieurs à cette date. Le filtre facultatif `statut` utilise les valeurs suivantes.

- `EN_RETARD` si la date de rappel est passée.
- `A_RAPPELER` si le rappel est prévu aujourd'hui ou dans les sept prochains jours.
- `A_JOUR` si la date de rappel est plus éloignée ou absente.

La tâche quotidienne rafraîchit les statuts à 08 h 00, selon l'heure du serveur. Avec le profil `dev`, elle journalise chaque événement `RappelVaccination` simulé une seule fois pour la date de rappel courante. Un changement de cette date réinitialise `rappelPublie`. La consultation des rappels est en lecture seule. RabbitMQ sera raccordé lors de l'intégration.

## Suivre un traitement

Créer un traitement dans `POST /api/vet/treatments`.

```json
{
  "dossierId": 1,
  "medicament": "Médicament prescrit",
  "posologie": "Selon ordonnance",
  "dateDebut": "2026-11-25",
  "dateFin": "2026-12-05"
}
```

La date de fin est facultative. Un traitement est créé avec le statut `EN_COURS` et apparaît dans `GET /api/vet/records/{id}/treatments`. Pour le suspendre, envoyer `{"statut":"SUSPENDU"}` à `PATCH /api/vet/treatments/{id}/status`.

Les transitions suivantes sont retenues pour le développement en attendant la validation de l'équipe. Un traitement en cours peut être suspendu ou terminé. Un traitement suspendu peut reprendre ou être terminé. `TERMINE` est final. Renvoyer le statut courant retourne `200`. Une transition interdite retourne `409`.
