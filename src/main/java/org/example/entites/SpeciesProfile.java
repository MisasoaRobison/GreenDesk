package org.example.entites;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "species")
public class SpeciesProfile {

    @Id
    private String id;

    private String name;
    private double baseGrowthRate;
    private double maxHeight;
    private double seedProductionRate;
    private double tempMin;
    private double tempMax;
    private double lightNeedLux;

    // Constructeur vide requis par MongoDB
    public SpeciesProfile() {}

    public SpeciesProfile(String name, double baseGrowthRate, double maxHeight,
                          double seedProductionRate, double tempMin, double tempMax,
                          double lightNeedLux) {
        this.name = name;
        this.baseGrowthRate = baseGrowthRate;
        this.maxHeight = maxHeight;
        this.seedProductionRate = seedProductionRate;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.lightNeedLux = lightNeedLux;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBaseGrowthRate() { return baseGrowthRate; }
    public void setBaseGrowthRate(double baseGrowthRate) { this.baseGrowthRate = baseGrowthRate; }

    public double getMaxHeight() { return maxHeight; }
    public void setMaxHeight(double maxHeight) { this.maxHeight = maxHeight; }

    public double getSeedProductionRate() { return seedProductionRate; }
    public void setSeedProductionRate(double seedProductionRate) { this.seedProductionRate = seedProductionRate; }

    public double getTempMin() { return tempMin; }
    public void setTempMin(double tempMin) { this.tempMin = tempMin; }

    public double getTempMax() { return tempMax; }
    public void setTempMax(double tempMax) { this.tempMax = tempMax; }

    public double getLightNeedLux() { return lightNeedLux; }
    public void setLightNeedLux(double lightNeedLux) { this.lightNeedLux = lightNeedLux; }

    // Méthodes utilitaires
    public boolean isTempOptimal(double temp) {
        return temp >= tempMin && temp <= tempMax;
    }

    public double tempStressFactor(double temp) {
        if (temp < tempMin) return (tempMin - temp) / 10.0;
        if (temp > tempMax) return (temp - tempMax) / 10.0;
        return 0.0;
    }

    public double lightStressFactor(double lux) {
        if (lux < lightNeedLux) return (lightNeedLux - lux) / 1000.0;
        return 0.0;
    }
}
