# MS4 · Suivi vétérinaire

Service Spring Boot 3.5 pour le suivi vétérinaire de Refugia. L'application utilise Java 17 et PostgreSQL 16. Elle écoute sur le port `8040`.

## Tests

Depuis ce dossier, avec `JAVA_HOME` pointant vers un JDK 17, lancer la commande PowerShell suivante.

```powershell
.\mvnw.cmd test
```

Les tests activent le profil `test`, qui utilise une base H2 en mémoire en mode PostgreSQL. Ils vérifient le démarrage, Swagger et le parcours d'un dossier médical.

## Démarrage local

Créer une base PostgreSQL 16 avec un utilisateur autorisé à y créer des tables. MS4 initialise la table `dossier_medical` au démarrage. L'URL par défaut est `jdbc:postgresql://localhost:5432/veterinaire_db`. Définir `MS4_DB_URL` si la base utilise une autre URL.

```powershell
$env:MS4_DB_USER = Read-Host 'Utilisateur PostgreSQL'
$motDePasse = Read-Host 'Mot de passe PostgreSQL' -AsSecureString
$env:MS4_DB_PASSWORD = [System.Net.NetworkCredential]::new('', $motDePasse).Password
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

Ouvrir [Swagger UI](http://localhost:8040/swagger-ui.html) ou consulter le document [OpenAPI JSON](http://localhost:8040/v3/api-docs).

## Essayer le parcours d'un dossier

Avec le profil local `dev`, MS4 simule la vérification de MS1 pour les animaux `1`, `2` et `12`. Sans ce profil, l'ouverture renvoie `503` tant que le client MS1 n'est pas intégré. Les autorisations Keycloak seront ajoutées lors de l'intégration.

1. Dans `POST /api/vet/records`, envoyer `{"animalId":12}` et relever l'`id` du dossier retourné.
2. Lire le dossier avec `GET /api/vet/records/{id}` ou avec `GET /api/vet/records/by-animal/12`.
3. Dans `PATCH /api/vet/records/{id}`, envoyer `{"notesGenerales":"Suivi à planifier"}`. La valeur `null` efface les notes.

Une seconde ouverture pour le même animal renvoie `409`.
