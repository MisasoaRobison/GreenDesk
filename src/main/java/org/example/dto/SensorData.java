package org.example.dto;

public class SensorData {
    private double humidity;
    private double temperature;
    private double luminosity;

    public SensorData() {
    }

    public SensorData(double humidity, double temperature, double luminosity){
        this.humidity = humidity;
        this.temperature = temperature;
        this.luminosity = luminosity;
    }

    public double getHumidity() {
        return humidity;
    }
    public double getTemperature() {
        return temperature;
    }
    public double getLuminosity(){
        return luminosity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    public void setLuminosity(double luminosity) {
        this.luminosity = luminosity;
    }
}
