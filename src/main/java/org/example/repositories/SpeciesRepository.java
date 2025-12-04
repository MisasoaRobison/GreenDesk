package org.example.repositories;

import org.example.entites.SpeciesProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesRepository extends MongoRepository<SpeciesProfile, String> {
}
