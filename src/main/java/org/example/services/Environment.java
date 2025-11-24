package org.example.services;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Environment {
    private double dayTemperature;
    private double nightTemperature;
    private double dayLuminosity;
    private double nightLuminosity;
    private double baseHumidity;
    private double humidityAmplitude;
    private double temperatureNoise;
    private double luminosityNoise;
    private double humidityNoise;

    public static Environment defaultProfile() {
        return new Environment(
                24.0,  // dayTemperature
                18.0,  // nightTemperature
                450.0, // dayLuminosity
                50.0,  // nightLuminosity
                50.0,  // baseHumidity
                10.0,  // humidityAmplitude
                1.5,   // temperatureNoise
                20.0,  // luminosityNoise
                5.0    // humidityNoise
        );
    }
}
