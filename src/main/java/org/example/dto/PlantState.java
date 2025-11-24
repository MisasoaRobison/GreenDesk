package org.example.dto;

public class PlantState {
    private SensorData sensors;
    private GrowthState growth;

    public PlantState() {
    }

    public PlantState(SensorData sensors, GrowthState growth){
        this.sensors = sensors;
        this.growth = growth;
    }

    public SensorData getSensors() {
        return sensors;
    }

    public GrowthState getGrowth() {
        return growth;
    }

    public void setSensors(SensorData sensors){
        this.sensors = sensors;
    }

    public void setGrowth(GrowthState growth) {
        this.growth = growth;
    }
}
