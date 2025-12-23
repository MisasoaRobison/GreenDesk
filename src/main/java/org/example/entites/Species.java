package org.example.entites;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;


@Document(collection ="species")
public class Species {
    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank (message = "on doit avoir le nom de l'espece")
    private String name;

    private double optimalWaterNeeds;
    private double optimalTemperature;
    private double optimalHumidity;
    private double optimalLuxNeeds;
    private double baseGrowthRate; // cm par cycle (par ex. par heure ou par jour)
    private double seedProductionRate;

    //--------------CONSTRCUTEURS--------------

    //Constructeur complet : à utiliser lorsqu'on connait exactement une espece de plante et tous les attributs
    public Species(String name, double optimalWaterNeeds, double optimalTemperature, double optimalHumidity, double optimalLuxNeeds, double baseGrowthRate, double seedProductionRate) {
        this.name = name;
        this.optimalWaterNeeds = optimalWaterNeeds; //en mL
        this.optimalTemperature = optimalTemperature; //en °C
        this.optimalHumidity = optimalHumidity; //en %
        this.optimalLuxNeeds = optimalLuxNeeds; //en lux
        this.baseGrowthRate = baseGrowthRate;
        this.seedProductionRate = seedProductionRate; //entre 0-1
    }

    //Constrcuteur à utiliser pour faire des tests, tout est généré aléatoirement à part le nom
    public Species(String name){
        this.name = name;
        this.optimalWaterNeeds = 100 + Math.random()*400;
        this.optimalTemperature = 15 + Math.random()*15;
        this.optimalHumidity = 30 + Math.random() * 50;
        this.optimalLuxNeeds = 1000 + Math.random() * 9000;
        this.baseGrowthRate = 0.1 + Math.random() * 0.9;
        this.seedProductionRate = 0.1 + Math.random()*0.9;
    }
    protected Species() {}


    public boolean isOptimalWaterNeeds(double waterLevel) {
        return Math.abs(optimalWaterNeeds - waterLevel) <= 15; //±15 de tolérance en mL
    }

    public boolean isOptimalTemperature(double temperature) {
        return Math.abs(optimalTemperature - temperature) <= 4; //±4°C de tolérance
    }

    public boolean isOptimalHumidity(double humidity) {
        return Math.abs(optimalHumidity - humidity) <= 10;//±10% de tolérance
    }

    public double tempStressFactor(double temperature) {
        double diff = Math.abs(temperature - optimalTemperature);
        double tolerance = 4; // ± tolérance définie dans Species
        if (diff <= tolerance) return 0.0; // pas de stress dans la plage tolérée

        // Stress normalisé : au-delà de la tolérance, on utilise le ratio de l’écart par rapport à l’optimal
        return Math.min(1.0, (diff - tolerance) / optimalTemperature);
    }

    public double humidityStressFactor(double humidity) {
        double diff = Math.abs(humidity - optimalHumidity);
        double tolerance = 10; // ± tolérance définie dans Species
        if (diff <= tolerance) return 0.0; // pas de stress

        return Math.min(1.0, (diff - tolerance) / optimalHumidity);
    }


    public double lightStressFactor(double lux){
        return Math.max(0, (optimalLuxNeeds - lux)/optimalLuxNeeds);//entre 0-1 pour le stress en raison d'un manque de lumière le cas échéant
    }

    //--------------Getters et Setters--------------
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public double getOptimalWaterNeeds(){
        return optimalWaterNeeds;
    }
    public double getOptimalTemperature(){
        return optimalTemperature;
    }
    public double getOptimalHumidity(){
        return optimalHumidity;
    }
    public double getOptimalLuxNeeds(){
        return optimalLuxNeeds;
    }
    public double getSeedProductionRate(){
        return seedProductionRate;
    }
    public double getBaseGrowthRate() {
        return baseGrowthRate;
    }
}
