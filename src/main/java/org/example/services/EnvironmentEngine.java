package org.example.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.dto.EnvironmentSnapshot;
import org.example.dto.SeasonProfile;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentEngine {

    private final List<SeasonProfile> seasons = new ArrayList<>();
    private boolean manualOverride = false;
    private EnvironmentSnapshot manualSnapshot;

    public EnvironmentEngine() {
        // Ajouter des saisons par défaut
        seasons.add(new SeasonProfile("Winter", 5, 3, 60, 5, 8));
        seasons.add(new SeasonProfile("Spring", 15, 5, 50, 10, 12));
        seasons.add(new SeasonProfile("Summer", 25, 5, 40, 5, 14));
        seasons.add(new SeasonProfile("Autumn", 15, 5, 50, 10, 10));
    }

    public EnvironmentSnapshot generateSnapshot(LocalDateTime time) {
        if (manualOverride && manualSnapshot != null) return manualSnapshot;

        SeasonProfile season = getSeasonForDate(time);
        double temp = computeTemperature(time, season);
        double lux = computeLightIntensity(time, season);
        double rainfall = Math.random() * season.getMaxDailyRain();
        double humidity = computeHumidity(temp, rainfall);
        double wind = Math.random() * 5;

        return new EnvironmentSnapshot(time, temp, humidity, lux, rainfall, wind);
    }

    private SeasonProfile getSeasonForDate(LocalDateTime time) {
        // Simplification : retourne la saison basée sur le mois
        int month = time.getMonthValue();
        switch(month) {
            case 12: case 1: case 2: return seasons.get(0);
            case 3: case 4: case 5: return seasons.get(1);
            case 6: case 7: case 8: return seasons.get(2);
            default: return seasons.get(3);
        }
    }

    private double computeTemperature(LocalDateTime t, SeasonProfile season) {
        double hourFraction = t.getHour() / 24.0;
        double base = season.getTempAvg() + season.getTempVar() * Math.sin(hourFraction * 2 * Math.PI);
        double noise = (Math.random() - 0.5) * 2;
        return base + noise;
    }

    private double computeLightIntensity(LocalDateTime t, SeasonProfile season) {
        int hour = t.getHour();
        if (hour < 6 || hour > 18) return 0;
        return 1000 * Math.sin((hour - 6) / 12.0 * Math.PI) + (Math.random() * 50 - 25);
    }

    private double computeHumidity(double temp, double rainfall) {
        double humidity = 50 + rainfall * 10 - (temp - 20) * 1.5;
        return Math.max(0, Math.min(100, humidity));
    }

    public void setManualSnapshot(EnvironmentSnapshot snapshot) {
        this.manualSnapshot = snapshot;
        this.manualOverride = true;
    }

    public void clearManualOverride() {
        this.manualOverride = false;
    }
}
