package org.example.services;

import org.example.repestories.*;
import org.example.entites.*;
import org.example.dto.*;
import org.springframework.stereotype.Service;
import lombok.*;

@Service
public class PlantServices {
    private PlantRepestory plant;

    private SensorSimulator sensorSimulator;
    private GrowthEngine growthEngine;

    public PlantServices(PlantRepestory plant, SensorSimulator sensorSimulator, GrowthEngine growthEngine){
        this.plant = plant;
        this.sensorSimulator = sensorSimulator;
        this.growthEngine = growthEngine;
    }

    public Plant createPlant(Plant plant) {
        return this.plant.save(plant);
    }

    public PlantState getState(Long id){
        Plant plant = this.plant.findById(id).orElseThrow(()->new RuntimeException("Plant not found"));
        SensorData sensors = sensorSimulator.readSensorData(plant);
        GrowthState growth = growthEngine.calculateGrowth(plant, sensors);

        return new PlantState(sensors, growth);
    }
}
