package org.example.controllers;

import org.example.repositories.*;
import org.example.entites.*;
import org.example.services.*;
import org.example.dto.*;
import org.springframework.web.bind.annotation.*; // @RestController, @RequestMapping, @PostMapping, @RequestBody
@RestController
@RequestMapping("/plants")
public class PlantController {
    private PlantServices plantServices;

    public PlantController(PlantServices plantServices) {
        this.plantServices = plantServices;
    }

    @PostMapping
    public Plant createPlant(@RequestBody Plant plant) {
        return plantServices.createPlant(plant);
    }

    @GetMapping("/{id}/state")
    public PlantState getState(@PathVariable String id){
        return plantServices.getState(id);
    }
}
