package org.example.dto;

public class SeasonProfile {
    private String name;
    private double tempAvg;
    private double tempVar;
    private double humidityAvg;
    private double maxDailyRain;
    private int daylightHours;

    public SeasonProfile(String name, double tempAvg, double tempVar, double humidityAvg,
                         double maxDailyRain, int daylightHours) {
        this.name = name;
        this.tempAvg = tempAvg;
        this.tempVar = tempVar;
        this.humidityAvg = humidityAvg;
        this.maxDailyRain = maxDailyRain;
        this.daylightHours = daylightHours;
    }

    // Getters
    public String getName() { return name; }
    public double getTempAvg() { return tempAvg; }
    public double getTempVar() { return tempVar; }
    public double getHumidityAvg() { return humidityAvg; }
    public double getMaxDailyRain() { return maxDailyRain; }
    public int getDaylightHours() { return daylightHours; }

    // Setters si besoin d’évolution dynamique
    public void setTempAvg(double tempAvg) { this.tempAvg = tempAvg; }
    public void setTempVar(double tempVar) { this.tempVar = tempVar; }
    public void setHumidityAvg(double humidityAvg) { this.humidityAvg = humidityAvg; }
    public void setMaxDailyRain(double maxDailyRain) { this.maxDailyRain = maxDailyRain; }
    public void setDaylightHours(int daylightHours) { this.daylightHours = daylightHours; }
}
