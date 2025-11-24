package org.example.dto;

public class GrowthState {
    private double newHeight;
    private double growthRate;

    public GrowthState() {
    }

    public GrowthState(double newHeight, double growthRate) {
        this.newHeight = newHeight;
        this.growthRate = growthRate;
    }

    public double getNewHeight() {
        return newHeight;
    }
    public double getGrowthRate() {
        return growthRate;
    }
    public void setNewHeight(double newHeight) {
        this.newHeight = newHeight;
    }
    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }
}
