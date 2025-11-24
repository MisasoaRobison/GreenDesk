package org.example.services;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

import org.example.dto.SensorData;
import org.example.entites.Plant;
import org.example.entites.SensorReading;
import org.example.repositories.SensorReadingRepository;
import org.springframework.stereotype.Component;

@Component
public class SensorSimulator {
    private final Environment environment;
    private final SensorReadingRepository sensorReadingRepository;
    private final Random random;
    private final Clock clock;

    public SensorSimulator(SensorReadingRepository sensorReadingRepository) {
        this(environmentDefault(), sensorReadingRepository, new Random(), Clock.systemDefaultZone());
    }

    public SensorSimulator(Environment environment,
                           SensorReadingRepository sensorReadingRepository,
                           Random random,
                           Clock clock) {
        this.environment = environment;
        this.sensorReadingRepository = sensorReadingRepository;
        this.random = random;
        this.clock = clock;
    }

    public SensorData readSensorData(Plant plant){
        LocalDateTime now = LocalDateTime.now(clock);
        double dayFactor = dayNightFactor(now.toLocalTime());

        double temperature = interpolate(environment.getNightTemperature(), environment.getDayTemperature(), dayFactor)
                + noise(environment.getTemperatureNoise());
        double luminosity = interpolate(environment.getNightLuminosity(), environment.getDayLuminosity(), dayFactor)
                + noise(environment.getLuminosityNoise());
        double humidity = environment.getBaseHumidity()
                + environment.getHumidityAmplitude() * Math.cos(Math.PI * dayFactor)
                + noise(environment.getHumidityNoise());

        SensorReading reading = new SensorReading(
                null,
                plant.getId(),
                now,
                humidity,
                temperature,
                luminosity
        );
        sensorReadingRepository.save(reading);

        return new SensorData(humidity, temperature, luminosity);
    }

    private static Environment environmentDefault() {
        return Environment.defaultProfile();
    }

    private double dayNightFactor(LocalTime time) {
        double ratio = time.toSecondOfDay() / 86400.0;
        // Midday (ratio ~0.5) peaks at 1, midnight at 0
        return 0.5 + 0.5 * Math.sin(2 * Math.PI * (ratio - 0.25));
    }

    private double interpolate(double nightValue, double dayValue, double factor) {
        return nightValue + (dayValue - nightValue) * factor;
    }

    private double noise(double amplitude) {
        return (random.nextDouble() * 2 - 1) * amplitude;
    }
}
