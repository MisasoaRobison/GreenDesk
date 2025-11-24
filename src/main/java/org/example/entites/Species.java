package org.example.entites;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "species")
public class Species {
    @Id
    private String id;

    @NotBlank(message = "Le nom de l'espèce est obligatoire")
    private String name; // le nom de l'espèce

    @PositiveOrZero(message = "Les besoins en eau doivent être positifs ou nuls")
    private double waterNeeds; // les besoins en eau

    // Les temperatures min et max
    @Field("TempMin")
    private double tempMin;

    @Field("TempMax")
    private double tempMax;

    // Les luminosités min et max
    @Field("LuminosityMin")
    private double luminosityMin;

    @Field("LuminosityMax")
    private double luminosityMax;

    public Species(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters et Setters explicites pour la compatibilité IDE
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWaterNeeds() {
        return waterNeeds;
    }

    public void setWaterNeeds(double waterNeeds) {
        this.waterNeeds = waterNeeds;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getLuminosityMin() {
        return luminosityMin;
    }

    public void setLuminosityMin(double luminosityMin) {
        this.luminosityMin = luminosityMin;
    }

    public double getLuminosityMax() {
        return luminosityMax;
    }

    public void setLuminosityMax(double luminosityMax) {
        this.luminosityMax = luminosityMax;
    }
}
