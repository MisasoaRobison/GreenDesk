package org.example.services;

import org.example.repositories.*;
import org.example.entites.*;
import org.example.dto.*;
import org.springframework.stereotype.Component;

@Component
public class GrowthEngine {
    public GrowthState calculateGrowth(Plant plant, SensorData sensorData) {
        double newHeight = plant.getHeight() +
                (sensorData.getHumidity()/100.0);
        return new GrowthState(newHeight, 0.01);
    }
}
