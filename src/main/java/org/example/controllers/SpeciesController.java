package org.example.controllers;

import org.example.entites.SpeciesProfile;
import org.example.repositories.SpeciesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/species")
public class SpeciesController {

    private final SpeciesRepository speciesRepo;

    public SpeciesController(SpeciesRepository speciesRepo) {
        this.speciesRepo = speciesRepo;
    }

    // -------------------------------------------------------------------------
    // CREATE - POST /species
    // -------------------------------------------------------------------------
    @PostMapping
    public SpeciesProfile createSpecies(@RequestBody SpeciesProfile species) {

        if (species.getName() == null || species.getName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Species name is required");
        }

        if (speciesRepo.existsById(species.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Species already exists");
        }

        return speciesRepo.save(species);
    }

    // -------------------------------------------------------------------------
    // READ ALL - GET /species
    // -------------------------------------------------------------------------
    @GetMapping
    public List<SpeciesProfile> getAllSpecies() {
        return speciesRepo.findAll();
    }

    // -------------------------------------------------------------------------
    // READ ONE - GET /species/{name}
    // -------------------------------------------------------------------------
    @GetMapping("/{name}")
    public SpeciesProfile getSpeciesByName(@PathVariable String name) {
        return speciesRepo.findById(name)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Species not found"
                ));
    }

    // -------------------------------------------------------------------------
    // UPDATE - PUT /species/{name}
    // -------------------------------------------------------------------------
    @PutMapping("/{name}")
    public SpeciesProfile updateSpecies(@PathVariable String name,
                                        @RequestBody SpeciesProfile update) {

        SpeciesProfile species = speciesRepo.findById(name)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Species not found"
                ));

        // Mise à jour partielle
        if (update.getBaseGrowthRate() > 0)
            species.setBaseGrowthRate(update.getBaseGrowthRate());

        if (update.getMaxHeight() > 0)
            species.setMaxHeight(update.getMaxHeight());

        if (update.getSeedProductionRate() > 0)
            species.setSeedProductionRate(update.getSeedProductionRate());

        if (update.getTempMin() > 0)
            species.setTempMin(update.getTempMin());

        if (update.getTempMax() > 0)
            species.setTempMax(update.getTempMax());

        if (update.getLightNeedLux() > 0)
            species.setLightNeedLux(update.getLightNeedLux());

        return speciesRepo.save(species);
    }

    // -------------------------------------------------------------------------
    // DELETE - DELETE /species/{name}
    // -------------------------------------------------------------------------
    @DeleteMapping("/{name}")
    public void deleteSpecies(@PathVariable String name) {
        if (!speciesRepo.existsById(name)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Species not found");
        }
        speciesRepo.deleteById(name);
    }
}
