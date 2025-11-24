package org.example.services;

import org.example.dto.*;
import org.example.entites.*;
import org.example.exceptions.ResourceNotFoundException;
import org.example.repositories.*;
import org.springframework.stereotype.Service;

@Service
public class PlantServices {
    private final PlantRepository plantRepository;
    private final SensorSimulator sensorSimulator;
    private final GrowthEngine growthEngine;

    public PlantServices(PlantRepository plantRepository, SensorSimulator sensorSimulator, GrowthEngine growthEngine){
        this.plantRepository = plantRepository;
        this.sensorSimulator = sensorSimulator;
        this.growthEngine = growthEngine;
    }

    public Plant createPlant(Plant plant) {
        return this.plantRepository.save(plant);
    }

    public PlantState getState(String id){
        Plant foundPlant = this.plantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plante introuvable"));
        SensorData sensors = sensorSimulator.readSensorData(foundPlant);
        GrowthState growth = growthEngine.calculateGrowth(foundPlant, sensors);

        return new PlantState(sensors, growth);
    }
}
