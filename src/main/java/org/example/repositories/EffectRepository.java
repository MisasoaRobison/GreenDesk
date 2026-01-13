package org.example.repositories;

import org.example.entites.Effect;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EffectRepository extends MongoRepository<Effect, String> {
    Optional<Effect> findByName(String name);
}
