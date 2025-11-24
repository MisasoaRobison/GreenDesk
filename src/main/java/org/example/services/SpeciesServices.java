package org.example.services;

import org.example.repositories.*;
import org.example.entites.*;
import org.springframework.stereotype.Service;


@Service
public class SpeciesServices {
    private final SpeciesRepository speciesRepository;

    public SpeciesServices(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }

    public Species createSpecies(Species species){
        return this.speciesRepository.save(species);
    }
}
