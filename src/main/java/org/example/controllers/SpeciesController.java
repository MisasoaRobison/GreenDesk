package org.example.controllers;

import org.example.repositories.*;
import org.example.entites.*;
import org.example.services.*;
import org.example.dto.*;
import org.springframework.web.bind.annotation.*; // @RestController, @RequestMapping, @PostMapping, @RequestBody

@RestController
@RequestMapping("/species")
public class SpeciesController {
    private SpeciesServices speciesServices;
    public SpeciesController(SpeciesServices speciesServices){
        this.speciesServices = speciesServices;
    }

    @PostMapping
    public Species createSpecies(@RequestBody Species species){
        return speciesServices.createSpecies(species);
    }
}
