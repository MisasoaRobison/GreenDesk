package org.example.repositories;

import org.example.entites.Species;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpeciesRepository extends MongoRepository<Species, String> {
    Optional<Species> findByName(String name); // pour éviter doublons
}
