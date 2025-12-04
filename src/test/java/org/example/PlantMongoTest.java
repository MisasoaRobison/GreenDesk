package org.example;

import org.example.entites.Plant;
import org.example.entites.SpeciesProfile;
import org.example.repositories.PlantRepository;
import org.example.repositories.SpeciesRepository;
import org.example.dto.EnvironmentSnapshot;
import org.example.services.EnvironmentEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Utiliser éventuellement application-test.properties
public class PlantMongoTest {

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private SpeciesRepository speciesRepository;

    private EnvironmentEngine envEngine;

    @BeforeEach
    public void setup() {
        plantRepository.deleteAll();
        speciesRepository.deleteAll();
        envEngine = new EnvironmentEngine();
    }

    @Test
    public void testCreatePlantsAndSimulateGrowth() {
        // 1️⃣ Créer une espèce et la sauvegarder
        SpeciesProfile tomato = new SpeciesProfile("Tomato", 2.0, 100.0, 0.5, 18, 28, 800);
        speciesRepository.save(tomato);

        // 2️⃣ Créer une plante liée à l'espèce et la sauvegarder
        Plant plant = new Plant("TomatoPlant1", tomato, 10.0, LocalDate.now());
        plantRepository.save(plant);

        // 3️⃣ Vérifier que la plante est bien persistée
        List<Plant> plantsFromDb = plantRepository.findAll();
        assertEquals(1, plantsFromDb.size());
        assertEquals("TomatoPlant1", plantsFromDb.get(0).getName());

        // 4️⃣ Simuler 3 jours de croissance
        LocalDateTime now = LocalDateTime.now();
        for (int day = 0; day < 3; day++) {
            EnvironmentSnapshot snapshot = envEngine.generateSnapshot(now.plusDays(day));
            plant.receiveStimulus(snapshot);
            var delta = plant.dailyTick(snapshot);
            plant.applyGrowthDelta(delta);
        }

        // 5️⃣ Vérifier l'état final
        System.out.printf("Height: %.2f | Leaves: %d | Stress: %.2f | State: %s%n",
                plant.getHeightCm(),
                plant.getLeavesCount(),
                plant.getStressIndex(),
                plant.getState()
        );

        assertTrue(plant.getHeightCm() >= 10.0);
        assertTrue(plant.getLeavesCount() >= 0);
        assertTrue(plant.getStressIndex() >= 0 && plant.getStressIndex() <= 1);
        assertNotNull(plant.getState());
    }
}
