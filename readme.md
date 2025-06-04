# SpaceExplorer API

Une API RESTful développée avec Spring Boot 3.4.x et Gradle, permettant de gérer des Planètes, Missions spatiales, Satellites, Observations et Utilisateurs. Intègre également des fonctionnalités d’IA via Mistral AI pour proposer des suggestions de mission et des conseils d’observation. La documentation interactive est exposée via Swagger/OpenAPI, et la base de données en développement est une instance H2 en mémoire.

---

## Table des matières

1. [Description générale](#description-générale)
2. [Fonctionnalités principales](#fonctionnalités-principales)
3. [Stack technologique & Dépendances](#stack-technologique--dépendances)
4. [Prérequis](#prérequis)
5. [Installation & Démarrage](#installation--démarrage)
6. [Fichier de configuration](#fichier-de-configuration)
7. [Accès à la base H2](#accès-à-la-base-h2)
8. [Documentation Swagger (OpenAPI)](#documentation-swagger-openapi)
9. [Endpoints disponibles](#endpoints-disponibles)

   1. [Auth](#1-auth)
   2. [User (Utilisateur)](#2-user-utilisateur)
   3. [Planet (Planète)](#3-planet-planète)
   4. [Mission (Mission spatiale)](#4-mission-mission-spatiale)
   5. [Satellite](#5-satellite)
   6. [Observation](#6-observation)
   7. [AI services (IA)](#7-ai-services-ia)
10. [Sécurité (JWT)](#sécurité-jwt)
11. [Postman Collection](#postman-collection)
12. [Déploiement](#déploiement)
13. [Contributeurs & Licence](#contributeurs--licence)

---

## Description générale

**SpaceExplorer API** est un back-end Java/Spring Boot conçu pour gérer un univers fictif de planètes, de missions spatiales, de satellites et d’observations, tout en proposant des fonctionnalités d’intelligence artificielle pour enrichir l’expérience. Elle permet de :

* Gérer les utilisateurs (CRUD + JWT, rôles ADMIN/USER)
* CRUD sur les Planètes (nom, type, description, dates d’audit)
* CRUD sur les Missions spatiales (piloté par un User, ciblant une ou plusieurs planètes)
* CRUD sur les Satellites (rattachés à une Planète)
* CRUD sur les Observations (réalisées par un User sur une Planète)
* Suggestions IA :

  * Plans de mission spatiale basés sur des critères
  * Conseils d’observation astronomique (instrument, lieu, date)
  * Chat libre basé sur Mistral AI

La base de données utilisée en local est **H2 en mémoire**, avec persistance `jdbc:h2:mem:spaceapi;DB_CLOSE_DELAY=-1`. La documentation interactive est exposée via **Swagger UI**.

---

## Fonctionnalités principales

* **Auth & sécurité**

  * Inscription (`/auth/register`)
  * Connexion + génération de JWT (`/auth/login`)
  * Rôles `ROLE_USER` / `ROLE_ADMIN`
  * Protection des endpoints via JWT + `@PreAuthorize`
* **Gestion des ressources**

  * **User** : créer, lire, mettre à jour, supprimer et lister les utilisateurs
  * **Planet** : créer, lire, mettre à jour, supprimer et lister les planètes
  * **Mission** : créer, lire, mettre à jour, supprimer et lister les missions spatiales, avec relations vers User (pilot) et Planet(s)
  * **Satellite** : créer, lire, mettre à jour, supprimer et lister les satellites, rattachés à une planète
  * **Observation** : créer, lire, mettre à jour, supprimer et lister des observations, reliées à un observateur (User) et une planète
* **Intelligence artificielle (Mistral AI)**

  * `POST /ai/mission-suggestions` → génère un plan de mission spatiale
  * `POST /ai/observation-tips` → propose des conseils d’observation astronomique
  * `POST /ai/chat` → chat libre avec l’IA
* **Documentation OpenAPI/Swagger**

  * Accessible à [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
  * Permet de tester tous les endpoints en ligne

---

## Stack technologique & Dépendances

* **Langage & Build** : Java 17, Gradle
* **Framework principal** : Spring Boot 3.4.x

  * Spring Web (Spring MVC)
  * Spring Data JPA (Hibernate)
  * Spring Security + JJWT (JWT)
  * Spring Validation
  * Springdoc OpenAPI / Swagger UI
  * Spring AI – Mistral AI Spring Boot Starter
* **Base de données** : H2 (mémoire)
* **Dépendances principales** (Gradle) :
  implementation 'org.springframework.boot\:spring-boot-starter-web'
  implementation 'org.springframework.boot\:spring-boot-starter-data-jpa'
  implementation 'org.springframework.boot\:spring-boot-starter-security'
  implementation 'org.springframework.boot\:spring-boot-starter-validation'
  implementation 'com.h2database\:h2'
  implementation 'io.jsonwebtoken\:jjwt-api:0.12.6'
  runtimeOnly 'io.jsonwebtoken\:jjwt-impl:0.12.6'
  runtimeOnly 'io.jsonwebtoken\:jjwt-jackson:0.12.6'
  implementation 'org.springdoc\:springdoc-openapi-starter-webmvc-ui:2.8.6'
  implementation 'org.springframework.ai\:spring-ai-mistral-ai-spring-boot-starter:1.0.0-M6'

---

## Prérequis

* Java 17 (JDK 17) installé
* Gradle 7+ (si vous utilisez l’invocation `gradlew`, aucun prérequis local pour Gradle)
* Clé API **Mistral AI** valide (propriété `spring.ai.mistralai.api-key`)
* IDE ou éditeur de code (IntelliJ IDEA, VS Code, Eclipse, etc.)
* (Optionnel) Postman pour tester les endpoints

---

## Installation & Démarrage

1. **Cloner le projet**
   git clone [https://github.com/](https://github.com/)<votre-utilisateur>/spaceexplorer-api.git
   cd spaceexplorer-api

2. **Copier / configurer la clé Mistral AI**
   Dans `src/main/resources/application.yaml`, modifiez :
   spring:
   ai:
   mistralai:
   api-key: "VOTRE\_CLE\_MISTRAL\_API\_ICY"
   chat:
   options:
   model: mistral-small-latest

   Remplacez `"VOTRE_CLE_MISTRAL_API_ICY"` par votre clé fournie par Mistral AI (au moins 32 caractères).

3. **Lancer en local**
   Depuis la racine du projet :
   ./gradlew clean bootRun

   * L’application démarre sur le port **8080**.
   * H2 s’initialise en mémoire, la base `jdbc:h2:mem:spaceapi` est créée.
   * Deux rôles (`ROLE_USER`, `ROLE_ADMIN`) sont insérés à la première exécution (via `CommandLineRunner`).

4. **(Optionnel) exécuter les tests**
   ./gradlew test

   * Les tests unitaires (sécurité, JPA, etc.) s’exécutent.

---

## Fichier de configuration

Le principal fichier est `src/main/resources/application.yaml` :

spring:
application:
name: SpaceExplorerAPI

datasource:
url: jdbc\:h2\:mem\:spaceapi;DB\_CLOSE\_DELAY=-1
driver-class-name: org.h2.Driver
username: sa
password:

jpa:
show-sql: true
defer-datasource-initialization: true
hibernate:
ddl-auto: update
properties:
hibernate:
dialect: org.hibernate.dialect.H2Dialect
format\_sql: true

h2:
console:
enabled: true    # H2 Console accessible à /h2-console

ai:
mistralai:
api-key: "VOTRE\_CLE\_MISTRALAPI"  # Remplacez ici
chat:
options:
model: mistral-small-latest

jwt:
secret: "Kj83HwPQ9sZ2vL0fT5gR7nBcD4LmY8Qx"   # Clé HMAC pour signer les JWT (au moins 32 octets)
expiration: 3600000   # 1 heure en millisecondes

springdoc:
api-docs:
path: /v3/api-docs
swagger-ui:
path: /swagger-ui.html

server:
port: 8080
forward-headers-strategy: framework

* **`jwt.secret`** : chaîne aléatoire d’au moins 32 caractères pour signer/valider les tokens HMAC-SHA256.
* **`jwt.expiration`** : durée de validité des JWT (en millisecondes).

---

## Accès à la base H2

* **URL** : `http://localhost:8080/h2-console`
* **JDBC URL** : `jdbc:h2:mem:spaceapi`
* **Utilisateur** : `sa`
* **Mot de passe** : (laisser vide)

Vous pourrez visualiser les tables générées automatiquement par Hibernate :

* `roles`
* `users`
* `user_roles`
* `planets`
* `missions`
* `mission_planets`
* `satellites`
* `observations`

---

## Documentation Swagger (OpenAPI)

Dès que l’application tourne, ouvrez :

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

* **Authorize** (en haut à droite) : collez votre JWT sous la forme
  Bearer \<VOTRE\_JWT\_ICI>
* Vous verrez les sections (Tags) :

  * **Auth services** (sans cadenas)
  * **User services**
  * **Planet services**
  * **Mission services**
  * **Satellite services**
  * **Observation services**
  * **AI services**

Chaque endpoint est documenté avec son modèle de requête et de réponse, ainsi que les codes HTTP possibles.

---

## Endpoints disponibles

### 1. Auth

#### 1.1 `POST /auth/register`

* **Description** : créer un nouvel utilisateur.
* **Accès** : public (pas de JWT).
* **Request Body** (JSON) :
  {
  "username": "alice",
  "email": "[alice@example.com](mailto:alice@example.com)",
  "password": "SecurePass123",
  "roles": \["user"]          // ou \["admin"] pour créer un administrateur
  }
* **Response** :

  * **200 OK**
    { "message": "User registered successfully!" }
  * **400 Bad Request** si `username` ou `email` existe déjà.

#### 1.2 `POST /auth/login`

* **Description** : authentifier un utilisateur et obtenir un JWT.
* **Accès** : public.
* **Request Body** (JSON) :
  {
  "username": "alice",
  "password": "SecurePass123"
  }
* **Response** :

  * **200 OK**
    {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9…",
    "type": "Bearer",
    "username": "alice",
    "roles": \["ROLE\_USER"]
    }
  * **401 Unauthorized** si identifiants invalides.

---

### 2. User (Utilisateur)

**Base URL prefix** : `/users`

> Note : ces endpoints requièrent un JWT valide dans l’en-tête `Authorization: Bearer <token>`.

| Méthode | URI           | Description                        | Rôle requis                 |
| ------- | ------------- | ---------------------------------- | --------------------------- |
| GET     | `/users/{id}` | Récupère l’utilisateur par son ID  | `ROLE_USER` ou `ROLE_ADMIN` |
| GET     | `/users`      | Liste tous les utilisateurs        | `ROLE_ADMIN`                |
| POST    | `/users`      | Crée un nouvel utilisateur         | `ROLE_ADMIN`                |
| PUT     | `/users/{id}` | Met à jour un utilisateur existant | `ROLE_ADMIN`                |
| DELETE  | `/users/{id}` | Supprime un utilisateur existant   | `ROLE_ADMIN`                |

#### Exemple de requête `GET /users/2`

GET [http://localhost:8080/users/2](http://localhost:8080/users/2)
Authorization: Bearer \<JWT\_VALID>

Réponse 200 OK :
{
"id": 2,
"username": "bob",
"email": "[bob@example.com](mailto:bob@example.com)",
"firstName": "Bob",
"lastName": "Martin",
"createdDate": "2025-06-03T14:30:00",
"updatedDate": "2025-06-03T14:30:00",
"roles": \["ROLE\_USER"]
}

#### Exemple de requête `POST /users`

POST [http://localhost:8080/users](http://localhost:8080/users)
Authorization: Bearer \<JWT\_ADMIN>
Content-Type: application/json

{
"username": "charlie",
"email": "[charlie@example.com](mailto:charlie@example.com)",
"firstName": "Charlie",
"lastName": "Chaplin",
"password": "Abcd1234!",
"roles": \["ROLE\_USER"]
}

Réponse 201 Created (exemple) :
{
"id": 3,
"username": "charlie",
"email": "[charlie@example.com](mailto:charlie@example.com)",
"firstName": "Charlie",
"lastName": "Chaplin",
"createdDate": "2025-06-03T15:00:00",
"updatedDate": "2025-06-03T15:00:00",
"roles": \["ROLE\_USER"]
}

---

### 3. Planet (Planète)

**Base URL prefix** : `/planets`

| Méthode | URI             | Description                      | Rôle requis                 |
| ------- | --------------- | -------------------------------- | --------------------------- |
| GET     | `/planets/{id}` | Récupère une planète par ID      | `ROLE_USER` ou `ROLE_ADMIN` |
| GET     | `/planets`      | Liste toutes les planètes        | `ROLE_USER` ou `ROLE_ADMIN` |
| POST    | `/planets`      | Crée une nouvelle planète        | `ROLE_ADMIN`                |
| PUT     | `/planets/{id}` | Met à jour une planète existante | `ROLE_ADMIN`                |
| DELETE  | `/planets/{id}` | Supprime une planète existante   | `ROLE_ADMIN`                |

#### Structure de `PlanetDto` (request body pour POST & PUT)

{
"name": "Mars",
"type": "Terrestre",
"description": "Planète rouge du système solaire"
}

#### Exemple de création (`POST /planets`)

POST [http://localhost:8080/planets](http://localhost:8080/planets)
Authorization: Bearer \<JWT\_ADMIN>
Content-Type: application/json

{
"name": "Mars",
"type": "Terrestre",
"description": "Planète rouge du système solaire"
}

Réponse 201 Created (body JSON) :

{
"id": 1,
"name": "Mars",
"type": "Terrestre",
"description": "Planète rouge du système solaire",
"createdDate": "2025-06-03T15:10:00",
"updatedDate": "2025-06-03T15:10:00"
}

---

### 4. Mission (Mission spatiale)

**Base URL prefix** : `/missions`

| Méthode | URI              | Description                      | Rôle requis                 |
| ------- | ---------------- | -------------------------------- | --------------------------- |
| GET     | `/missions/{id}` | Récupère une mission par ID      | `ROLE_USER` ou `ROLE_ADMIN` |
| GET     | `/missions`      | Liste toutes les missions        | `ROLE_USER` ou `ROLE_ADMIN` |
| POST    | `/missions`      | Crée une nouvelle mission        | `ROLE_USER` ou `ROLE_ADMIN` |
| PUT     | `/missions/{id}` | Met à jour une mission existante | `ROLE_USER` ou `ROLE_ADMIN` |
| DELETE  | `/missions/{id}` | Supprime une mission existante   | `ROLE_ADMIN`                |

#### Structure de `MissionDto` (request body pour POST & PUT)

{
"name": "Mars Expedition",
"launchDate": "2025-09-01",         // format ISO YYYY-MM-DD
"description": "Mission robotique vers Mars",
"pilotUsername": "alice",            // username d’un utilisateur existant
"planetIds": \[1, 2]                  // ID des planètes ciblées (ex : Mars, Jupiter)
}

#### Exemple de création (`POST /missions`)

POST [http://localhost:8080/missions](http://localhost:8080/missions)
Authorization: Bearer \<JWT\_ALICE>
Content-Type: application/json

{
"name": "Mars Expedition",
"launchDate": "2025-09-01",
"description": "Mission robotique vers Mars",
"pilotUsername": "alice",
"planetIds": \[1]
}

Réponse 201 Created (body JSON) :

{
"id": 1,
"name": "Mars Expedition",
"launchDate": "2025-09-01",
"description": "Mission robotique vers Mars",
"createdDate": "2025-06-03T15:20:00",
"updatedDate": "2025-06-03T15:20:00",
"pilot": {
"id": 2,
"username": "alice"
},
"planets": \[
{
"id": 1,
"name": "Mars"
}
]
}

---

### 5. Satellite

**Base URL prefix** : `/satellites`

| Méthode | URI                | Description                      | Rôle requis                 |
| ------- | ------------------ | -------------------------------- | --------------------------- |
| GET     | `/satellites/{id}` | Récupère un satellite par ID     | `ROLE_USER` ou `ROLE_ADMIN` |
| GET     | `/satellites`      | Liste tous les satellites        | `ROLE_USER` ou `ROLE_ADMIN` |
| POST    | `/satellites`      | Crée un nouveau satellite        | `ROLE_ADMIN`                |
| PUT     | `/satellites/{id}` | Met à jour un satellite existant | `ROLE_ADMIN`                |
| DELETE  | `/satellites/{id}` | Supprime un satellite existant   | `ROLE_ADMIN`                |

#### Structure de `SatelliteDto` (request body pour POST & PUT)

{
"name": "Europa",
"type": "Naturel",
"planetId": 1                     // ID d’une planète existante
}

#### Exemple de création (`POST /satellites`)

POST [http://localhost:8080/satellites](http://localhost:8080/satellites)
Authorization: Bearer \<JWT\_ADMIN>
Content-Type: application/json

{
"name": "Europa",
"type": "Naturel",
"planetId": 1
}

Réponse 201 Created (body JSON) :

{
"id": 1,
"name": "Europa",
"type": "Naturel",
"createdDate": "2025-06-03T15:30:00",
"updatedDate": "2025-06-03T15:30:00",
"planet": {
"id": 1,
"name": "Mars"
}
}

---

### 6. Observation

**Base URL prefix** : `/observations`

| Méthode | URI                  | Description                          | Rôle requis                 |
| ------- | -------------------- | ------------------------------------ | --------------------------- |
| GET     | `/observations/{id}` | Récupère une observation par ID      | `ROLE_USER` ou `ROLE_ADMIN` |
| GET     | `/observations`      | Liste toutes les observations        | `ROLE_USER` ou `ROLE_ADMIN` |
| POST    | `/observations`      | Crée une nouvelle observation        | `ROLE_USER` ou `ROLE_ADMIN` |
| PUT     | `/observations/{id}` | Met à jour une observation existante | `ROLE_USER` ou `ROLE_ADMIN` |
| DELETE  | `/observations/{id}` | Supprime une observation existante   | `ROLE_ADMIN`                |

#### Structure de `ObservationDto` (request body pour POST & PUT)

{
"type": "Image",
"result": "[http://exemple.com/jupiter.jpg](http://exemple.com/jupiter.jpg)",
"observerUsername": "alice",     // username de l’observateur existant
"planetId": 1                    // ID de la planète observée
}

#### Exemple de création (`POST /observations`)

POST [http://localhost:8080/observations](http://localhost:8080/observations)
Authorization: Bearer \<JWT\_ALICE>
Content-Type: application/json

{
"type": "Image",
"result": "[http://exemple.com/jupiter.jpg](http://exemple.com/jupiter.jpg)",
"observerUsername": "alice",
"planetId": 1
}

Réponse 201 Created (body JSON) :

{
"id": 1,
"timestamp": "2025-06-03T15:40:00",
"type": "Image",
"result": "[http://exemple.com/jupiter.jpg](http://exemple.com/jupiter.jpg)",
"createdDate": "2025-06-03T15:40:00",
"updatedDate": "2025-06-03T15:40:00",
"observer": {
"id": 2,
"username": "alice"
},
"planet": {
"id": 1,
"name": "Mars"
}
}

---

### 7. AI services (IA)

**Base URL prefix** : `/ai`

| Méthode | URI                       | Description                                                 | Rôle requis                 |
| ------- | ------------------------- | ----------------------------------------------------------- | --------------------------- |
| POST    | `/ai/mission-suggestions` | Génère un plan de mission spatiale en fonction des critères | `ROLE_USER` ou `ROLE_ADMIN` |
| POST    | `/ai/observation-tips`    | Conseils d’observation astronomique selon paramètres        | `ROLE_USER` ou `ROLE_ADMIN` |
| POST    | `/ai/chat`                | Chat libre avec Mistral AI                                  | `ROLE_USER` ou `ROLE_ADMIN` |

#### 7.1 `POST /ai/mission-suggestions`

* **Request Body** (JSON) :
  {
  "targetPlanet": "Mars",
  "missionType": "exploration",
  "durationDays": 30,
  "objectives": "Étudier la composition du sol, rechercher de la vie microbienne"
  }
* **Response 200 OK** (Texte généré par l’IA) :

  1. Introduction

     * Mission “Mars Exploration 2025” : …
  2. Phase de préparation

     * Équipe : ingénieurs, scientifiques…
     * …
       (Texte complet renvoyé par Mistral)
* **400 Bad Request** si le corps JSON est invalide.

#### 7.2 `POST /ai/observation-tips`

* **Request Body** (JSON) :
  {
  "target": "Jupiter",
  "instrument": "télescope optique 200mm",
  "observationDate": "2025-08-10",
  "location": "Désert d'Atacama, Chili"
  }
* **Response 200 OK** (Texte généré par l’IA) :
  Pour observer Jupiter le 10 août 2025 depuis le désert d’Atacama :

  * Conditions météo : …
  * Utiliser un filtre rouge pour …
  * …

#### 7.3 `POST /ai/chat`

* **Request Body** (chaîne brute, pas de DTO) :
  Quel est le meilleur itinéraire pour envoyer une sonde vers Europe (satellite de Jupiter) ?
* **Response 200 OK** (Texte généré par l’IA) :
  Étape 1 : Lancement depuis la Terre en utilisant un lanceur type Ariane 6…
  Étape 2 : Trajectoire de type VEEGA (Venus-Earth-Earth Gravity Assist)…
  3\. …

---

## Sécurité (JWT)

L’API utilise **JSON Web Tokens (JWT)** pour sécuriser la majeure partie des endpoints.

* Lors du **login** (`POST /auth/login`), un token JWT signé HMAC-SHA256 est généré (HS256).
* Le **secret** pour signer/valider se trouve dans `application.yaml` (propriété `jwt.secret`). Il doit contenir au moins 32 octets (par exemple : `"Kj83HwPQ9sZ2vL0fT5gR7nBcD4LmY8Qx"`).
* Le token expire après `jwt.expiration` millisecondes (par défaut `3600000` = 1 heure).
* Chaque requête aux endpoints protégés doit inclure un header HTTP :
  Authorization: Bearer \<VOTRE\_JWT\_ICI>
* Les **rôles** sont gérés par Spring Security et stockés sous forme d’une collection d’`authorities` dans le payload du JWT.

  * **ROLE\_USER** : accès en lecture (GET) aux principales ressources, création de certaines entités (Missions, Observations).
  * **ROLE\_ADMIN** : accès complet (CRUD) sur toutes les ressources.

---

## Postman Collection

Une collection Postman a été préparée pour faciliter les tests :

* **Nom du fichier** : `SpaceExplorerAPI.postman_collection.json`
* **Contenu** : exemples de requêtes pour tous les endpoints (Auth, User, Planet, Mission, Satellite, Observation, AI).
* **Importer dans Postman** :

  1. Ouvrez Postman → **File** → **Import**
  2. Sélectionnez `SpaceExplorerAPI.postman_collection.json`
  3. La collection apparaît dans votre sidebar.
  4. Modifiez la variable d’environnement `{{base_url}}` pour `http://localhost:8080`
  5. Dans l’onglet **Authorization** de la collection, configurez le type `Bearer Token`

     * Définissez la variable `{{jwt_token}}` (mise à jour après avoir fait `POST /auth/login`).
  6. Lancez les requêtes dans l’ordre :

     * `Auth → Register`
     * `Auth → Login` (copier le token dans `{{jwt_token}}`)
     * Tester Planet, Mission, Satellite, Observation, AI

---

## Déploiement

### 1. Préparer pour la production

* **Base de données** : remplacer H2 en mémoire par PostgreSQL, MySQL ou autre.
  Exemple (PostgreSQL) dans `application.yaml` :
  spring:
  datasource:
  url: jdbc\:postgresql://<db-host>:5432/spaceapi
  username: <db-user>
  password: <db-password>
  driver-class-name: org.postgresql.Driver

  jpa:
  hibernate:
  ddl-auto: update  # ou validate, en fonction de vos besoins
  properties:
  hibernate:
  dialect: org.hibernate.dialect.PostgreSQLDialect

* **Secrets & variables d’environnement** :

  * `JWT_SECRET` (remplacer `jwt.secret`)
  * `JWT_EXPIRATION` (durée de validité)
  * `SPRING_AI_MISTRALAI_API-KEY` (clé Mistral AI)
  * Toute autre variable (ex. DB\_URL, DB\_USERNAME, DB\_PASSWORD)

### 2. Déploiement sur un PaaS (Railway, Heroku, Render…)

1. **Créez un projet/app** sur la plateforme choisie.
2. **Connectez votre repo GitHub** & activez déploiement automatique sur la branche `main`.
3. **Configurez les variables d’environnement** :

   * `SPRING_DATASOURCE_URL` (si vous utilisez une base externe)
   * `SPRING_DATASOURCE_USERNAME`
   * `SPRING_DATASOURCE_PASSWORD`
   * `JWT_SECRET`
   * `SPRING_AI_MISTRALAI_API-KEY`
   * `SPRING_PROFILES_ACTIVE=prod` (optionnel, si vous distinguez dev et prod)
4. **Build & Run** : la plateforme détecte Gradle + Spring Boot et exécute `./gradlew bootRun` ou crée un jar exécutable.
5. Une fois déployé, votre API est accessible via une URL publique (ex : `https://space-explorer-api.onrender.com`).
6. **Swagger UI en production** : par défaut, `springdoc.swagger-ui.enable` est `true` en profil `dev`. En prod, pensez à désactiver ou protéger Swagger UI pour ne pas l’exposer publiquement.

---

## Contributeurs & Licence

* **Auteur principal** : Alex
* **Contributeurs** : (Ajouter noms / GitHub si nécessaire)
* **Licence** : MIT License
  MIT License

  Copyright (c) 2025 Alex

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.

---

**Félicitations !** Vous disposez désormais d’une API complète pour gérer un univers spatial, sécurisée, documentée, et enrichie par l’IA. N’hésitez pas à ouvrir des issues/pull requests pour suggérer des améliorations ou ajouter des fonctionnalités supplémentaires (pagination, filtres, tri, tests unitaires avancés, intégration front-end, etc.).
