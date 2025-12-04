package org.example;

import org.example.entites.Plant;
import org.example.entites.SpeciesProfile;
import org.example.dto.EnvironmentSnapshot;
import org.example.services.EnvironmentEngine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SimulationRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Créer des espèces
        SpeciesProfile tomato = new SpeciesProfile("Tomato", 2.0, 100.0, 0.5, 18, 28, 800);
        SpeciesProfile lettuce = new SpeciesProfile("Lettuce", 1.0, 30.0, 0.3, 10, 25, 500);

        // Créer des plantes
        List<Plant> plants = new ArrayList<>();
        plants.add(new Plant("TomatoPlant1", tomato, 10.0, LocalDate.now()));
        plants.add(new Plant("LettucePlant1", lettuce, 5.0, LocalDate.now()));

        // Créer l'environnement
        EnvironmentEngine envEngine = new EnvironmentEngine();
        LocalDateTime currentTime = LocalDateTime.now();

        // Simuler 7 jours
        for (int day = 0; day < 7; day++) {
            System.out.println("=== Day " + (day + 1) + " ===");
            for (Plant plant : plants) {
                EnvironmentSnapshot snapshot = envEngine.generateSnapshot(currentTime);
                plant.receiveStimulus(snapshot);
                var delta = plant.dailyTick(snapshot);
                plant.applyGrowthDelta(delta);
                plant.tryReproduce();

                // Afficher état
                System.out.printf("%s | Height: %.2f | Leaves: %d | Stress: %.2f | State: %s | SeedBank: %d%n",
                        plant.getName(),
                        plant.getHeightCm(),
                        plant.getLeavesCount(),
                        plant.getStressIndex(),
                        plant.getState(),
                        plant.getSeedBank()
                );
            }
            currentTime = currentTime.plusDays(1);
        }
    }
}
