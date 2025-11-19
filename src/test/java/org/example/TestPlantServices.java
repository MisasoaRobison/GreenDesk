package org.example;

import org.example.entites.*;
import org.example.repositories.*;
import org.example.services.*;
import org.example.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class TestPlantServices {

    @Autowired
    private PlantServices plantServices;

    @Autowired
    private SpeciesRepository speciesRepository;

    @Test
    public void testCreateAndGetState() {

        Species species = new Species(null, "Flower");
        species = speciesRepository.save(species);

        Plant plant = new Plant(null, "Tulip", species, 0.2, LocalDate.now());
        Plant saved = plantServices.createPlant(plant);

        PlantState state = plantServices.getState(saved.getId());
        assertNotNull(state);

        System.out.println(state);
    }
}