package org.example.repositories;

import org.example.entites.Plant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlantRepository extends MongoRepository<Plant, String> {
}
