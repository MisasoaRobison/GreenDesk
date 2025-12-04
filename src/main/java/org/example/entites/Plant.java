package org.example.entites;

import org.example.dto.EnvironmentSnapshot;
import org.example.dto.GrowthDelta;
import org.example.dto.PlantState;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("plant")
public class Plant {

    @Id
    private String id;

    private String name;
    private SpeciesProfile species;
    private double heightCm;
    private int leavesCount;
    private double stressIndex;
    private PlantState state;
    private int seedBank;
    private LocalDate plantedDate;

    // Obligatoire pour MongoDB
    public Plant() {}

    public Plant(String name, SpeciesProfile species, double heightCm, LocalDate plantedDate) {
        this.name = name;
        this.species = species;
        this.heightCm = heightCm;
        this.leavesCount = 0;
        this.stressIndex = 0.0;
        this.state = PlantState.HEALTHY;
        this.seedBank = 0;
        this.plantedDate = plantedDate;
    }

    // ========= MÉTHODES MÉTIER ========= //

    public void receiveStimulus(EnvironmentSnapshot env) {
        double stress = 0.0;

        if (!species.isTempOptimal(env.getTemperature()))
            stress += species.tempStressFactor(env.getTemperature());

        if (env.getLux() < species.getLightNeedLux())
            stress += species.lightStressFactor(env.getLux());

        this.stressIndex = Math.min(1.0, this.stressIndex + stress);
        updateState();
    }

    private void updateState() {
        if (stressIndex < 0.3) state = PlantState.HEALTHY;
        else if (stressIndex < 0.6) state = PlantState.STRESSED;
        else if (stressIndex < 0.9) state = PlantState.DORMANT;
        else state = PlantState.DISEASED;
    }

    public GrowthDelta dailyTick(EnvironmentSnapshot env) {
        GrowthDelta delta = new GrowthDelta();

        if (state == PlantState.DORMANT || state == PlantState.DISEASED)
            return delta;

        double growthFactor = species.getBaseGrowthRate() * (1 - stressIndex);

        delta.setHeightDelta(growthFactor);
        delta.setLeavesDelta((int) Math.round(growthFactor / 10));
        delta.setStressDelta(-0.05);

        return delta;
    }

    public void applyGrowthDelta(GrowthDelta delta) {
        this.heightCm = Math.min(species.getMaxHeight(), this.heightCm + delta.getHeightDelta());
        this.leavesCount += delta.getLeavesDelta();
        this.stressIndex = Math.max(0, this.stressIndex + delta.getStressDelta());
        updateState();
    }

    public boolean tryReproduce() {
        double chance = species.getSeedProductionRate() * (1 - stressIndex);
        if (Math.random() < chance) {
            seedBank++;
            return true;
        }
        return false;
    }

    // ========= GETTERS & SETTERS COMPLETS ========= //

    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public SpeciesProfile getSpecies() { return species; }
    public void setSpecies(SpeciesProfile species) { this.species = species; }

    public double getHeightCm() { return heightCm; }
    public void setHeightCm(double heightCm) { this.heightCm = heightCm; }

    public int getLeavesCount() { return leavesCount; }
    public void setLeavesCount(int leavesCount) { this.leavesCount = leavesCount; }

    public double getStressIndex() { return stressIndex; }
    public void setStressIndex(double stressIndex) { this.stressIndex = stressIndex; }

    public PlantState getState() { return state; }
    public void setState(PlantState state) { this.state = state; }

    public int getSeedBank() { return seedBank; }
    public void setSeedBank(int seedBank) { this.seedBank = seedBank; }

    public LocalDate getPlantedDate() { return plantedDate; }
    public void setPlantedDate(LocalDate plantedDate) { this.plantedDate = plantedDate; }

}
