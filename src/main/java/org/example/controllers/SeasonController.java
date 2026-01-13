package org.example.controllers;

import org.example.entites.Season;
import org.example.entites.SeasonCycle;
import org.example.services.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seasons")
public class SeasonController {
    
    @Autowired
    private SeasonService seasonService;
    
    /**
     * GET /api/seasons
     * Récupère le catalogue de toutes les saisons prédéfinies.
     */
    @GetMapping
    public ResponseEntity<List<Season>> getAllSeasons() {
        List<Season> seasons = seasonService.getAllSeasons();
        return ResponseEntity.ok(seasons);
    }
}
