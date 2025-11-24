package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Random;

import org.example.dto.SensorData;
import org.example.entites.Plant;
import org.example.entites.SensorReading;
import org.example.repositories.SensorReadingRepository;
import org.example.services.Environment;
import org.example.services.SensorSimulator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SensorSimulatorTest {

    @Mock
    private SensorReadingRepository sensorReadingRepository;

    @InjectMocks
    private SensorSimulator sensorSimulator;

    @Test
    void generatesDaytimeValuesAndPersistsReading() {
        Environment env = new Environment(
                24.0, 18.0, 450.0, 50.0,
                50.0, 10.0,
                0.0, 0.0, 0.0
        );
        // Fixed time: noon UTC
        Clock fixedClock = Clock.fixed(Instant.parse("2024-08-01T12:00:00Z"), ZoneOffset.UTC);
        Random fixedRandom = new Random(42); // deterministic noise

        sensorSimulator = new SensorSimulator(env, sensorReadingRepository, fixedRandom, fixedClock);

        Plant plant = new Plant("plant-1", "Tulip", null, 0.2, LocalDate.now(fixedClock));

        SensorData data = sensorSimulator.readSensorData(plant);

        // At noon, dayFactor is close to 1: values near day setpoints
        assertEquals(24.0, data.getTemperature(), 0.1);
        assertEquals(450.0, data.getLuminosity(), 0.1);
        assertEquals(40.0, data.getHumidity(), 0.1); // base + amplitude * cos(pi * 1)

        ArgumentCaptor<SensorReading> captor = ArgumentCaptor.forClass(SensorReading.class);
        verify(sensorReadingRepository).save(captor.capture());
        assertEquals(plant.getId(), captor.getValue().getPlantId());
    }
}
