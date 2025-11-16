package org.example.repestories;

import org.example.entites.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepestory extends JpaRepository<Plant, Long> {
}