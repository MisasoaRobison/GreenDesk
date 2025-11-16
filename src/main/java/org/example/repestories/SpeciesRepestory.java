package org.example.repestories;

import org.example.entites.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeciesRepestory extends JpaRepository<Species, Long> {
}