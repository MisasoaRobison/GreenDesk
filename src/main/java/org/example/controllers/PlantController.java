package org.example.controllers;

import org.example.dto.PlantState;
import org.example.entites.Plant;
import org.example.services.PlantServices;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping; // @RestController, @RequestMapping, @PostMapping, @RequestBody
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plants")
public class PlantController {
    private final PlantServices plantServices;

    public PlantController(PlantServices plantServices) {
        this.plantServices = plantServices;
    }

    @PostMapping
    public Plant createPlant(@Valid @RequestBody Plant plant) {
        return plantServices.createPlant(plant);
    }

    @GetMapping("/{id}/state")
    public PlantState getState(@PathVariable String id) {
        return plantServices.getState(id);
    }
}
