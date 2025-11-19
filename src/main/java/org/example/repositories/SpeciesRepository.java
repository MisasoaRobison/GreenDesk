package org.example.repositories;

import org.example.entites.Species;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpeciesRepository extends MongoRepository<Species, String> {
}
