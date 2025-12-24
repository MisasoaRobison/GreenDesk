package org.example.services;

import org.example.entites.Plant;
import org.example.entites.Species;
import org.example.entites.PlantState;
import org.example.entites.EnvironmentData;
import org.example.entites.Intervention;
import org.example.repositories.PlantRepository;
import org.example.repositories.SpeciesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlantServices {

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private SpeciesRepository speciesRepository;


    // Création avec toutes les valeurs explicites
    public Plant createPlant(String name, String speciesId,
                             double water, double temp, double humidity, double lux) throws Exception {
        Species species = speciesRepository.findById(speciesId)
                .orElseThrow(() -> new Exception("Espèce introuvable : " + speciesId));

        Plant plant = new Plant(name, species, water, temp, humidity, lux);
        plant.setPlantState(plant.evaluateState());

        return plantRepository.save(plant);
    }

    // Création avec seulement le nom et l'espèce (valeurs générées aléatoirement)
    public Plant createPlant(String name, String speciesId) throws Exception {
        Species species = speciesRepository.findById(speciesId)
                .orElseThrow(() -> new Exception("Espèce introuvable : " + speciesId));

        Plant plant = new Plant(name, species); // constructeur aléatoire
        plant.setPlantState(plant.evaluateState());

        return plantRepository.save(plant);
    }

    public void deletePlantById(String plantId) {
        plantRepository.deleteById(plantId);
    }

    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    public void deleteAllPlants() {
        plantRepository.deleteAll();
    }

    public Optional<Plant> getPlantById(String id) throws Exception {
        return Optional.ofNullable(plantRepository.findById(id).orElse(null));
    }

    public void evolvePlant(Plant plant, EnvironmentData env) {
        // 1️⃣ Calcul du stress pour chaque paramètre
        double waterStress = Math.abs(plant.getWaterLevel() - plant.getSpecies().getOptimalWaterNeeds())
                / plant.getSpecies().getOptimalWaterNeeds();

        double tempStress = plant.getSpecies().tempStressFactor(env.getTemperature());
        double humidityStress = plant.getSpecies().humidityStressFactor(env.getHumidity());
        double lightStress = plant.getSpecies().lightStressFactor(env.getLux());

        // 2️⃣ Stress total (moyenne)
        double totalStress = (waterStress + tempStress + humidityStress + lightStress) / 4.0;

        // 3️⃣ Mise à jour progressive du stressIndex
        double stressDelta = totalStress * 0.1; // facteur de progression par tick
        double newStress = plant.getStressIndex() + stressDelta;
        plant.setStressIndex(Math.min(1.0, Math.max(0.0, newStress)));

        updatePlantState(plant);

        // 5️⃣ Croissance proportionnelle au stress
        double growthFactor = plant.getSpecies().getBaseGrowthRate() * (1 - plant.getStressIndex());
        plant.setHeightCm(plant.getHeightCm() + growthFactor);

        plantRepository.save(plant);
    }


    private void updatePlantState(Plant plant) {
        double stress = plant.getStressIndex();
        if (stress < 0.3) plant.setPlantState(PlantState.HEALTHY);
        else if (stress < 0.6) plant.setPlantState(PlantState.STRESSED);
        else if (stress < 0.9) plant.setPlantState(PlantState.DORMANT);
        else plant.setPlantState(PlantState.DISEASED);
        plantRepository.save(plant);
    }

    public void applyIntervention(Plant plant, Intervention action) {
        switch(action.getType()) {
            case WATER:
                plant.setWaterLevel(plant.getWaterLevel() + action.getValue());
                break;
            case PRUNE:
                plant.setHeightCm(Math.max(0, plant.getHeightCm() - action.getValue()));
                break;
            case SHADING:
                plant.setLux(Math.max(0, plant.getLux() - action.getValue()));
                break;
        }
        plantRepository.save(plant);
    }

}
