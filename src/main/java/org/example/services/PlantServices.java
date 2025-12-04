package org.example.services;

import org.example.entites.Plant;
import org.example.dto.EnvironmentSnapshot;
import org.example.dto.GrowthDelta;
import java.util.ArrayList;
import java.util.List;

public class PlantServices {
    private final List<Plant> plants = new ArrayList<>();

    public void addPlant(Plant p) { plants.add(p); }

    public void simulateDay(EnvironmentSnapshot env) {
        for (Plant p : plants) {
            p.receiveStimulus(env);
            GrowthDelta delta = p.dailyTick(env);
            p.applyGrowthDelta(delta);
            p.tryReproduce();
        }
    }

    public List<Plant> getPlants() { return plants; }
}
