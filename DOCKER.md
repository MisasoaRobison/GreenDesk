# GreenDesk - Docker Deployment

## Architecture Docker

L'application est dockerisee avec 3 services :
- **app** : Application Spring Boot GreenDesk
- **mongodb** : Base de donnees MongoDB
- **mongo-express** : Interface web pour MongoDB

## Demarrage rapide

### Lancer tous les services
```bash
docker compose up -d
```

### Voir les logs
```bash
# Tous les services
docker compose logs -f

# Application uniquement
docker compose logs -f app

# MongoDB uniquement
docker compose logs -f mongodb
```

### Arreter les services
```bash
docker compose down
```

### Arreter et supprimer les donnees
```bash
docker compose down -v
```

## Acces aux services

| Service | URL | Identifiants |
|---------|-----|--------------|
| **API GreenDesk** | http://localhost:8080 | - |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | - |
| **Mongo Express** | http://localhost:8081 | admin / admin |
| **MongoDB** | mongodb://localhost:27017 | admin / adminpass |

## Commandes utiles

### Reconstruire l'application
```bash
docker compose build app
docker compose up -d app
```

### Voir l'etat des services
```bash
docker compose ps
```

### Se connecter a MongoDB
```bash
docker exec -it greendesk-mongodb mongosh -u admin -p adminpass
```

### Redemarrer un service
```bash
docker compose restart app
```

## Structure des volumes

Les donnees MongoDB sont persistees dans des volumes Docker :
- `mongodb_data` : Donnees de la base
- `mongodb_config` : Configuration MongoDB

## Configuration reseau

Tous les services communiquent via le reseau `greendesk-network`.

## Variables d'environnement

L'application peut etre configuree via des variables d'environnement dans `docker-compose.yml` :

```yaml
environment:
  SPRING_DATA_MONGODB_URI: mongodb://admin:adminpass@mongodb:27017/greendesk?authSource=admin
  SPRING_DATA_MONGODB_DATABASE: greendesk
```

## Depannage

### L'application ne demarre pas
```bash
# Verifier les logs
docker compose logs app

# Verifier que MongoDB est pret
docker compose logs mongodb
```

### Reinitialiser completement
```bash
docker compose down -v
docker compose up -d
```

### Reconstruire depuis zero
```bash
docker compose down -v
docker compose build --no-cache
docker compose up -d
```
