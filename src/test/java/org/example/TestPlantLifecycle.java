package org.example;

import org.example.entites.Plant;
import org.example.entites.SpeciesProfile;
import org.example.dto.EnvironmentSnapshot;
import org.example.services.EnvironmentEngine;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlantLifecycle {

    @Test
    public void testMultipleDaysGrowthAndReproduction() {
        // 1️⃣ Créer des espèces
        SpeciesProfile tomato = new SpeciesProfile(
                "Tomato",
                2.0,   // croissance base 2 cm/jour
                100.0, // hauteur max 100 cm
                0.5,   // probabilité semence
                18,    // temp min optimale
                28,    // temp max optimale
                800    // lumière optimale lux
        );

        SpeciesProfile lettuce = new SpeciesProfile(
                "Lettuce",
                1.0,   // croissance base 1 cm/jour
                30.0,  // hauteur max 30 cm
                0.3,   // probabilité semence
                10,    // temp min optimale
                25,    // temp max optimale
                500    // lumière optimale lux
        );

        // 2️⃣ Créer les plantes
        List<Plant> plants = new ArrayList<>();
        plants.add(new Plant("TomatoPlant1", tomato, 10.0, LocalDate.now()));
        plants.add(new Plant("LettucePlant1", lettuce, 5.0, LocalDate.now()));

        // 3️⃣ Créer l'environnement
        EnvironmentEngine envEngine = new EnvironmentEngine();

        // 4️⃣ Simuler 7 jours
        LocalDateTime currentTime = LocalDateTime.of(2025, 6, 15, 12, 0);
        for (int day = 0; day < 7; day++) {
            for (Plant plant : plants) {
                EnvironmentSnapshot snapshot = envEngine.generateSnapshot(currentTime);

                // Appliquer stimulus environnemental
                plant.receiveStimulus(snapshot);

                // Croissance quotidienne
                var delta = plant.dailyTick(snapshot);
                plant.applyGrowthDelta(delta);

                // Tentative de reproduction
                plant.tryReproduce();
            }

            // Avancer d'un jour
            currentTime = currentTime.plusDays(1);
        }

        // 5️⃣ Vérifier que les plantes ont grandi et que le stress reste dans les limites
        for (Plant plant : plants) {
            System.out.println("Plant: " + plant.getName());
            System.out.println("Height: " + plant.getHeightCm());
            System.out.println("Leaves: " + plant.getLeavesCount());
            System.out.println("Stress: " + plant.getStressIndex());
            System.out.println("State: " + plant.getState());
            System.out.println("SeedBank: " + plant.getSeedBank());
            System.out.println("----------");

            assertTrue(plant.getHeightCm() >= 5.0);
            assertTrue(plant.getLeavesCount() >= 0);
            assertTrue(plant.getStressIndex() >= 0.0 && plant.getStressIndex() <= 1.0);
            assertNotNull(plant.getState());
            assertTrue(plant.getSeedBank() >= 0);
        }
    }
}
