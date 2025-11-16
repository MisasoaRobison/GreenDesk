package org.example.services;

import org.example.repestories.*;
import org.example.entites.*;
import org.example.dto.*;
import org.springframework.stereotype.Component;

@Component
public class SensorSimulator {
    public SensorData readSensorData(Plant plant){
        return new SensorData(
                40 + Math.random() * 20,   // humidité 40-60%
                18 + Math.random() * 5,    // température 18-23°C
                200 + Math.random() * 100  // luminosité 200-300
        );
    }
}
