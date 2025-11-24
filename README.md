# GreenDesk 

Application **Spring Boot** pour la gestion et le suivi de la croissance des plantes.

## Description

**GreenDesk** est une application de gestion de plantes qui permet de :

- Créer et gérer des plantes avec leurs caractéristiques
- Gérer des espèces de plantes
- Simuler des données de capteurs (température, humidité, luminosité)
- Calculer la croissance des plantes en fonction des conditions environnementales
- Consulter l'état des plantes en temps réel

## Simulation Capteurs (Data & IoT simulé)

- Séries temporelles jour/nuit avec bruit configurable via `Environment`
- Génération de lectures capteurs (`SensorSimulator`) et persistance dans Mongo (`SensorReadingRepository`)
- Modèles: `SensorReading`, `SensorData`, `Environment`
- La croissance (`GrowthEngine`) s'appuie sur les données simulées (température + humidité)

## Technologies utilisées

- **Java** (Spring Boot 3.3.3)
- **Spring Boot Starter Web** - API REST
- **Spring Boot Starter Data MongoDB** - Persistance des données
- **MongoDB Atlas** - Base de données cloud
- **Spring Boot Starter Validation** - Validation des données
- **SpringDoc OpenAPI** (Swagger) - Documentation de l'API
- **Lombok** - Réduction du code boilerplate
- **JUnit 5** - Tests unitaires
- **Gradle** - Gestion des dépendances

## Structure du projet

```
src/
├── main/
│   ├── java/org/example/
│   │   ├── GreenDesk.java              # Classe principale Spring Boot
│   │   ├── controllers/                # Contrôleurs REST
│   │   │   ├── ApiExceptionHandler.java
│   │   │   ├── PlantController.java
│   │   │   └── SpeciesController.java
│   │   ├── dto/                        # Data Transfer Objects
│   │   │   ├── GrowthState.java
│   │   │   ├── PlantState.java
│   │   │   └── SensorData.java
│   │   ├── entites/                    # Entités MongoDB
│   │   │   ├── Plant.java
│   │   │   ├── Species.java
│   │   │   └── SensorReading.java
│   │   ├── repositories/               # Repositories MongoDB
│   │   │   ├── PlantRepository.java
│   │   │   ├── SpeciesRepository.java
│   │   │   └── SensorReadingRepository.java
│   │   └── services/                   # Logique métier
│   │       ├── Environment.java
│   │       ├── GrowthEngine.java
│   │       ├── PlantServices.java
│   │       ├── SensorSimulator.java
│   │       └── SpeciesServices.java
│   └── resources/
│       └── application.properties      # Configuration de l'application
└── test/
    └── java/org/example/
        ├── TestPlantServices.java
        ├── GrowthEngineTest.java
        ├── PlantControllerTest.java
        └── SensorSimulatorTest.java
```

## Installation et démarrage

### Prérequis

- Java 17 ou supérieur
- Gradle (inclus via Gradle Wrapper)
- Connexion internet (pour MongoDB Atlas) ou instance Mongo locale

### Étapes d'installation

1. **Cloner le projet**
   
   ```bash
   git clone <url-du-repo>
   cd GreenDesk
   ```

2. **Configuration de la base de données**
   
   Configurez via variables d'environnement (recommandé) :
   ```properties
   MONGODB_URI=mongodb://localhost:27017/greendesk_db
   MONGODB_DB=greendesk_db
   ```
   Si non définies, l'application utilisera ces valeurs par défaut.

3. **Compiler le projet**
   ```bash
   ./gradlew build
   ```

4. **Lancer l'application**
   ```bash
   ./gradlew bootRun
   ```

L'application démarre sur **http://localhost:8080**

## Documentation de l'API

Une fois l'application lancée, accédez à la documentation Swagger UI :

**http://localhost:8080/swagger-ui.html**

### Endpoints principaux

#### Plantes

- `POST /plants` - Créer une nouvelle plante
- `GET /plants/{id}/state` - Obtenir l'état actuel d'une plante
- Validation automatique des payloads + réponses **400** structurées (`ApiExceptionHandler`)

#### Espèces

- Endpoints pour gérer les espèces de plantes

#### Capteurs (simulation)

- La lecture se fait lors de `GET /plants/{id}/state`: le simulateur génère **humidité/luminosité/température**, les enregistre, puis calcule la croissance.

## Tests

Lancer les tests unitaires :

```bash
./gradlew test
```

Principaux tests :

- `TestPlantServices` (services métiers)
- `SensorSimulatorTest` (cycle **jour/nuit** + persistance)
- `GrowthEngineTest` (formule de croissance)
- `PlantControllerTest` (validation et format **API**)

## Configuration

Le fichier `application.properties` contient :

- Configuration **MongoDB Atlas**
- Port du serveur **(8080)**
- Configuration de sérialisation **JSON**

## Build

Créer un **JAR** exécutable :

```bash
./gradlew bootJar
```

Le **JAR** sera généré dans `build/libs/`

## Fonctionnalités principales

### Moteur de croissance

Le `GrowthEngine` calcule la croissance des plantes en fonction :

- De l'humidité
- De la hauteur actuelle
- Des caractéristiques de l'espèce

### Simulateur de capteurs

Le `SensorSimulator` génère des séries jour/nuit réalistes **(température, humidité, luminosité)** avec bruit configurable (`Environment`) et enregistre chaque lecture dans **MongoDB** (`SensorReading`).


## Licence

Ce projet est un **Projet académique**.

---
