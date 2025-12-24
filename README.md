# GreenDesk

Projet de gestion et simulation de plantes et d’espèces végétales.

## Version
v0.1


GreenDesk

Projet de gestion et simulation de plantes et d’espèces végétales.

Version

v1.0

🌱 GreenDesk – API de gestion et simulation de plantes

GreenDesk est une application Spring Boot + MongoDB qui permet de gérer des espèces végétales et des plantes, d’évaluer leur état (stress, santé) et d’exposer ces informations via une API REST.

Le projet met en pratique :

Une architecture Spring Boot claire (Controller / Service / Repository)

L’utilisation de MongoDB (Atlas ou local)

La modélisation métier (espèces, plantes, états, interventions)

1️⃣ Fonctionnalités implémentées
🌿 Gestion des espèces

Créer une espèce avec ses besoins optimaux :

Eau

Température

Humidité

Lumière

Taux de croissance et production de graines

Lister toutes les espèces

Récupérer une espèce par son nom

Mettre à jour une espèce existante

Supprimer une espèce

Stockage persistant dans MongoDB

🌱 Gestion des plantes

Créer une plante liée à une espèce existante

Initialisation automatique des valeurs environnementales (mode test)

Lister toutes les plantes

Récupérer une plante par son ID

Consulter l’état d’une plante (HEALTHY, STRESSED, DORMANT, DISEASED)

Mettre à jour une plante

Supprimer une plante

🧠 Logique métier

Calcul de l’état d’une plante en fonction :

Des besoins optimaux de l’espèce

Des valeurs environnementales actuelles

Stress calculé dynamiquement côté backend

Interventions possibles : arroser, tailler, réduire la lumière

2️⃣ Structure du projet
GreenDesk
├── controllers
│   ├── PlantController.java      # Endpoints REST pour les plantes
│   └── SpeciesController.java    # Endpoints REST pour les espèces
│
├── entites
│   ├── Plant.java                # Modèle plante + logique d’état
│   ├── Species.java              # Modèle espèce (besoins optimaux)
│   ├── PlantState.java           # Enum des états (HEALTHY, STRESSED…)
│   ├── EnvironmentData.java      # Données environnementales
│   └── Intervention.java         # Actions possibles sur une plante
│
├── repositories
│   ├── PlantRepository.java      # Accès MongoDB pour Plant
│   └── SpeciesRepository.java    # Accès MongoDB pour Species
│
├── services
│   ├── PlantServices.java        # Logique métier des plantes
│   ├── SpeciesServices.java      # Logique métier des espèces
│   ├── EnvironmentServices.java  # Évolution de l’environnement
│   └── Simulation.java           # Simulation manuelle ou horaire
│
├── GreenDesk.java                # Classe principale Spring Boot
├── resources
│   └── application.properties    # Configuration MongoDB & serveur
└── Test.java                     # Tests locaux (hors API REST)

3️⃣ Installation et lancement
Prérequis

Java 17+

Gradle

MongoDB Atlas ou MongoDB local

Étapes
git clone <repo-url>
cd GreenDesk


Configurer MongoDB dans :

src/main/resources/application.properties


Exemple :

spring.data.mongodb.uri=mongodb+srv://USER:PASSWORD@cluster.mongodb.net/
spring.data.mongodb.database=bdd_GreenDesk
server.port=8080


Lancer l’application :

./gradlew bootRun


➡️ L’API démarre sur http://localhost:8080

4️⃣ Utilisation de l’API (exemples)
🌿 Espèces
Créer une espèce
curl -X POST http://localhost:8080/api/species \
-H "Content-Type: application/json" \
-d '{
  "name": "Tomato",
  "optimalWaterNeeds": 200,
  "optimalTemperature": 22,
  "optimalHumidity": 60,
  "optimalLuxNeeds": 1500,
  "baseGrowthRate": 1.5,
  "seedProductionRate": 0.4
}'

Mettre à jour une espèce
curl -X PUT http://localhost:8080/api/species/ESPECE_ID \
-H "Content-Type: application/json" \
-d '{ "optimalWaterNeeds": 250 }'

Supprimer une espèce
curl -X DELETE http://localhost:8080/api/species/ESPECE_ID

Récupérer toutes les espèces
curl http://localhost:8080/api/species

Récupérer une espèce par nom
curl http://localhost:8080/api/species/Tomato

🌱 Plantes
Créer une plante (lié à une espèce)
curl -X POST "http://localhost:8080/plants/create?name=Tomato_Plant_1&speciesId=SPECIES_ID"


⚠️ SPECIES_ID doit être l’ID MongoDB réel de l’espèce.

Mettre à jour une plante
curl -X PUT "http://localhost:8080/plants/PLANT_ID?water=220&temperature=23"

Supprimer une plante
curl -X DELETE http://localhost:8080/plants/PLANT_ID

Récupérer toutes les plantes
curl http://localhost:8080/plants

Récupérer une plante par ID
curl http://localhost:8080/plants/PLANT_ID

Consulter l’état d’une plante
curl http://localhost:8080/plants/PLANT_ID/state


Résultat possible :

HEALTHY
STRESSED
DORMANT
DISEASED

5️⃣ Architecture et fonctionnement

Species : définit les besoins idéaux d’une plante et ses taux de croissance/production.

Plant : liée à une espèce, possède ses valeurs actuelles et son stressIndex.

PlantState : enum représentant l’état calculé dynamiquement selon l’écart aux besoins optimaux.

Services : centralisent la logique métier et les calculs.

Controllers : exposent les endpoints REST.

Repositories : accès MongoDB via Spring Data.

Simulation / EnvironmentServices : font évoluer l’environnement et les plantes automatiquement ou manuellement.