package org.example.controllers;

import org.example.entites.Effect;
import org.example.entites.PlantEffect;
import org.example.services.EffectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EffectController {
    
    @Autowired
    private EffectService effectService;
    
    /**
     * GET /api/effects
     * Récupère le catalogue de tous les effets disponibles.
     */
    @GetMapping("/effects")
    public ResponseEntity<List<Effect>> getAllEffects() {
        // Initialiser le catalogue s'il n'existe pas
        effectService.initializeEffectsCatalog();
        
        List<Effect> effects = effectService.getAllEffects();
        return ResponseEntity.ok(effects);
    }
    
    /**
     * POST /api/plants/{plantId}/effects/{effectId}
     * Applique un effet à une plante.
     */
    @PostMapping("/plants/{plantId}/effects/{effectId}")
    public ResponseEntity<?> applyEffectToPlant(
            @PathVariable String plantId,
            @PathVariable String effectId) {
        try {
            PlantEffect plantEffect = effectService.applyEffectToPlant(plantId, effectId);
            return ResponseEntity.status(HttpStatus.CREATED).body(plantEffect);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * GET /api/plants/{plantId}/effects
     * Récupère tous les effets d'une plante (actifs et inactifs).
     */
    @GetMapping("/plants/{plantId}/effects")
    public ResponseEntity<List<PlantEffect>> getPlantEffects(@PathVariable String plantId) {
        List<PlantEffect> effects = effectService.getPlantEffects(plantId);
        return ResponseEntity.ok(effects);
    }
    
    /**
     * GET /api/plants/{plantId}/effects/active
     * Récupère les effets actifs d'une plante.
     */
    @GetMapping("/plants/{plantId}/effects/active")
    public ResponseEntity<List<PlantEffect>> getActivePlantEffects(@PathVariable String plantId) {
        List<PlantEffect> effects = effectService.getActivePlantEffects(plantId);
        return ResponseEntity.ok(effects);
    }
    
    /**
     * DELETE /api/plants/effects/{plantEffectId}
     * Retire (désactive) un effet d'une plante.
     */
    @DeleteMapping("/plants/effects/{plantEffectId}")
    public ResponseEntity<?> removeEffectFromPlant(@PathVariable String plantEffectId) {
        try {
            effectService.removeEffectFromPlant(plantEffectId);
            return ResponseEntity.ok(Map.of("message", "Effet retiré avec succès"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
