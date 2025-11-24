package org.example.entites;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sensor_readings")
public class SensorReading {
    @Id
    private String id;

    @NotBlank
    private String plantId;

    @NotNull
    private LocalDateTime timestamp;

    private double humidity;
    private double temperature;
    private double luminosity;
}
