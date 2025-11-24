package org.example.repositories;

import org.example.entites.SensorReading;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SensorReadingRepository extends MongoRepository<SensorReading, String> {
}
