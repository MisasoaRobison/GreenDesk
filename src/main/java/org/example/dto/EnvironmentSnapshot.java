package org.example.dto;

import java.time.LocalDateTime;

public class EnvironmentSnapshot {
    private LocalDateTime timestamp;
    private double temperature; // °C
    private double humidity;    // %
    private double lux;         // lumière
    private double rainfall;    // mm
    private double windSpeed;   // m/s

    public EnvironmentSnapshot(LocalDateTime timestamp, double temperature, double humidity,
                               double lux, double rainfall, double windSpeed) {
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.humidity = humidity;
        this.lux = lux;
        this.rainfall = rainfall;
        this.windSpeed = windSpeed;
    }

    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getLux() { return lux; }
    public double getRainfall() { return rainfall; }
    public double getWindSpeed() { return windSpeed; }
}
