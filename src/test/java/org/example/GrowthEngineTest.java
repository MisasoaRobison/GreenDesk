package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.dto.GrowthState;
import org.example.dto.SensorData;
import org.example.entites.Plant;
import org.example.services.GrowthEngine;
import org.junit.jupiter.api.Test;

class GrowthEngineTest {

    private final GrowthEngine growthEngine = new GrowthEngine();

    @Test
    void computesGrowthRateFromHumidityAndTemperature() {
        Plant plant = new Plant();
        plant.setHeight(1.0);

        SensorData data = new SensorData(50, 25, 300);

        GrowthState result = growthEngine.calculateGrowth(plant, data);

        // humidity: 0.5 * 0.02 = 0.01 ; temperature: (25-10)/100*0.01 = 0.0015 ; base 0.01
        double expectedGrowthRate = 0.01 + 0.01 + 0.0015;
        assertEquals(expectedGrowthRate, result.getGrowthRate(), 0.0001);
        assertEquals(1.0 + expectedGrowthRate, result.getNewHeight(), 0.0001);
    }
}
