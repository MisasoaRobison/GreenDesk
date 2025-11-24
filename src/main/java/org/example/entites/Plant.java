package org.example.entites;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "plants")
public class Plant {
    @Id
    private String id;

    @NotBlank(message = "Le nom de la plante est obligatoire")
    private String name;

    @NotNull(message = "L'espèce est obligatoire")
    @DBRef
    private Species species;

    @PositiveOrZero(message = "La hauteur doit être positive ou nulle")
    private double height;

    @PastOrPresent(message = "La date de plantation ne peut pas être future")
    private LocalDate plantedDate;

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

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public LocalDate getPlantedDate() {
        return plantedDate;
    }

    public void setPlantedDate(LocalDate plantedDate) {
        this.plantedDate = plantedDate;
    }
}
