package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.example.dto.GrowthState;
import org.example.dto.PlantState;
import org.example.dto.SensorData;
import org.example.entites.Plant;
import org.example.entites.Species;
import org.example.repositories.PlantRepository;
import org.example.services.GrowthEngine;
import org.example.services.PlantServices;
import org.example.services.SensorSimulator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TestPlantServices {

    @Mock
    private PlantRepository plantRepository;

    @Mock
    private SensorSimulator sensorSimulator;

    @Mock
    private GrowthEngine growthEngine;

    @InjectMocks
    private PlantServices plantServices;

    @Test
    void testCreateAndGetState() {
        Species species = new Species("species-1", "Flower");
        Plant plant = new Plant(null, "Tulip", species, 0.2, LocalDate.now());
        Plant savedPlant = new Plant("plant-1", plant.getName(), plant.getSpecies(), plant.getHeight(), plant.getPlantedDate());

        when(plantRepository.save(plant)).thenReturn(savedPlant);
        when(plantRepository.findById("plant-1")).thenReturn(Optional.of(savedPlant));

        SensorData sensors = new SensorData(50, 20, 250);
        GrowthState growth = new GrowthState(0.25, 0.05);

        when(sensorSimulator.readSensorData(savedPlant)).thenReturn(sensors);
        when(growthEngine.calculateGrowth(savedPlant, sensors)).thenReturn(growth);

        Plant saved = plantServices.createPlant(plant);
        PlantState state = plantServices.getState(saved.getId());

        assertEquals(savedPlant.getId(), saved.getId());
        assertNotNull(state);
        assertEquals(growth.getNewHeight(), state.getGrowth().getNewHeight());
        assertEquals(sensors.getHumidity(), state.getSensors().getHumidity());
    }
}
