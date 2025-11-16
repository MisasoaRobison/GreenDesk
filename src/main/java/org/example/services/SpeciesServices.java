package org.example.services;

import org.example.repestories.*;
import org.example.entites.*;
import org.springframework.stereotype.Service;


@Service
public class SpeciesServices {
    private SpeciesRepestory species;

    public SpeciesServices(SpeciesRepestory species) {
        this.species = species;
    }

    public Species createSpecies(Species species){
        return this.species.save(species);
    }
}
