package org.example.services;

import org.example.entites.Plant;
import org.example.dto.*;
import org.springframework.stereotype.Component;

@Component
public class GrowthEngine {
    public GrowthState calculateGrowth(Plant plant, SensorData sensorData) {
        double humidityInfluence = sensorData.getHumidity() / 100.0;
        double temperatureInfluence = Math.max(0, (sensorData.getTemperature() - 10)) / 100.0;
        double growthRate = 0.01 + humidityInfluence * 0.02 + temperatureInfluence * 0.01;
        double newHeight = plant.getHeight() + growthRate;
        return new GrowthState(newHeight, growthRate);
    }
}
