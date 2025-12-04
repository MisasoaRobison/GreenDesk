package org.example.controllers;

import org.example.entites.Plant;
import org.example.entites.SpeciesProfile;
import org.example.repositories.PlantRepository;
import org.example.repositories.SpeciesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
//import java.util.Optional;

@RestController
@RequestMapping("/plants")
public class PlantController {

    private final PlantRepository plantRepo;
    private final SpeciesRepository speciesRepo;

    public PlantController(PlantRepository plantRepo, SpeciesRepository speciesRepo) {
        this.plantRepo = plantRepo;
        this.speciesRepo = speciesRepo;
    }

    // -------------------------------------------------------------------------
    // CREATE - POST /plants
    // -------------------------------------------------------------------------
    @PostMapping
    public Plant createPlant(@RequestBody Plant newPlantRequest) {

        if (newPlantRequest.getSpecies() == null ||
            newPlantRequest.getSpecies().getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Species is missing");
        }

        String speciesName = newPlantRequest.getSpecies().getName();

        SpeciesProfile species = speciesRepo.findById(speciesName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Species '" + speciesName + "' not found")
                );

        Plant plant = new Plant(
                newPlantRequest.getName(),
                species,
                newPlantRequest.getHeightCm(),
                LocalDate.now()
        );

        return plantRepo.save(plant);
    }

    // -------------------------------------------------------------------------
    // READ ALL - GET /plants
    // -------------------------------------------------------------------------
    @GetMapping
    public List<Plant> getAllPlants() {
        return plantRepo.findAll();
    }

    // -------------------------------------------------------------------------
    // READ ONE - GET /plants/{id}
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public Plant getPlantById(@PathVariable String id) {
        return plantRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Plant not found"));
    }

    // -------------------------------------------------------------------------
    // UPDATE - PUT /plants/{id}
    // -------------------------------------------------------------------------
    @PutMapping("/{id}")
    public Plant updatePlant(@PathVariable String id, @RequestBody Plant updatedFields) {

        Plant plant = plantRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Plant not found"));

        if (updatedFields.getHeightCm() > 0)
            plant.setHeightCm(updatedFields.getHeightCm());

        if (updatedFields.getLeavesCount() > 0)
            plant.setLeavesCount(updatedFields.getLeavesCount());

        if (updatedFields.getStressIndex() >= 0)
            plant.setStressIndex(updatedFields.getStressIndex());

        return plantRepo.save(plant);
    }

    // -------------------------------------------------------------------------
    // DELETE - DELETE /plants/{id}
    // -------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public void deletePlant(@PathVariable String id) {
        if (!plantRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plant not found");
        }
        plantRepo.deleteById(id);
    }
}
