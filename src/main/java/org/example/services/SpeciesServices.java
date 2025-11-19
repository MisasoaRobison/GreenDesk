package org.example.services;

import org.example.repositories.*;
import org.example.entites.*;
import org.springframework.stereotype.Service;


@Service
public class SpeciesServices {
    private SpeciesRepository species;

    public SpeciesServices(SpeciesRepository species) {
        this.species = species;
    }

    public Species createSpecies(Species species){
        return this.species.save(species);
    }
}
