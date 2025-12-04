package org.example.dto;

public class GrowthDelta {
    private double heightDelta;
    private int leavesDelta;
    private double stressDelta;

    public GrowthDelta() {
        this.heightDelta = 0;
        this.leavesDelta = 0;
        this.stressDelta = 0;
    }

    // Getters et setters
    public double getHeightDelta() {
        return heightDelta;
    }

    public void setHeightDelta(double heightDelta) {
        this.heightDelta = heightDelta;
    }

    public int getLeavesDelta() {
        return leavesDelta;
    }

    public void setLeavesDelta(int leavesDelta) {
        this.leavesDelta = leavesDelta;
    }

    public double getStressDelta() {
        return stressDelta;
    }

    public void setStressDelta(double stressDelta) {
        this.stressDelta = stressDelta;
    }
}
