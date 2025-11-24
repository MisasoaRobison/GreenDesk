package org.example.controllers;

import org.example.entites.Species;
import org.example.services.SpeciesServices;
import org.springframework.web.bind.annotation.*; // @RestController, @RequestMapping, @PostMapping, @RequestBody

import jakarta.validation.Valid;

@RestController
@RequestMapping("/species")
public class SpeciesController {
    private final SpeciesServices speciesServices;
    public SpeciesController(SpeciesServices speciesServices){
        this.speciesServices = speciesServices;
    }

    @PostMapping
    public Species createSpecies(@Valid @RequestBody Species species){
        return speciesServices.createSpecies(species);
    }
}
