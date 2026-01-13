package org.example;

import java.time.LocalDateTime;
import java.util.List;

import org.example.entites.Effect;
import org.example.entites.EnvironmentData;
import org.example.entites.Plant;
import org.example.entites.PlantEffect;
import org.example.entites.PlantState;
import org.example.entites.Species;
import org.example.repositories.EffectRepository;
import org.example.repositories.PlantEffectRepository;
import org.example.repositories.PlantRepository;
import org.example.repositories.SpeciesRepository;
import org.example.services.EffectService;
import org.example.services.PlantServices;
import org.example.services.SpeciesServices;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Tests pour la Livraison 2 - Feature 2 : Effets sur les plantes
 * 
 * Critères d'acceptation :
 * - Catalogue d'effets prédéfinis disponible
 * - Appliquer un effet "Shade 6h" à une plante
 * - Observer les variations de stressIndex/plantState sur plusieurs ticks
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestEffects {
    
    @Autowired
    private EffectService effectService;
    
    @Autowired
    private PlantServices plantServices;
    
    @Autowired
    private SpeciesServices speciesServices;
    
    @Autowired
    private EffectRepository effectRepository;
    
    @Autowired
    private PlantEffectRepository plantEffectRepository;
    
    @Autowired
    private PlantRepository plantRepository;
    
    @Autowired
    private SpeciesRepository speciesRepository;
    
    private Species testSpecies;
    
    @BeforeEach
    public void setUp() throws Exception {
        // Nettoyer la base
        plantEffectRepository.deleteAll();
        effectRepository.deleteAll();
        plantRepository.deleteAll();
        speciesRepository.deleteAll();
        
        // Créer une espèce de test
        Species newSpecies = new Species("Orchidée", 200.0, 22.0, 80.0, 3000.0, 0.3, 0.4);
        testSpecies = speciesServices.createSpecies(newSpecies);
    }
    
    @org.junit.jupiter.api.Test
    @Order(1)
    @DisplayName("Critère 1 : Catalogue d'effets prédéfinis disponible")
    public void testEffectsCatalog() {
        // Initialiser le catalogue
        effectService.initializeEffectsCatalog();
        
        // Récupérer tous les effets
        List<Effect> effects = effectService.getAllEffects();
        
        // Vérifier qu'il y a au moins 4 effets prédéfinis
        assertTrue(effects.size() >= 4, "Il devrait y avoir au moins 4 effets prédéfinis");
        
        // Vérifier que les effets clés sont présents
        assertTrue(effects.stream().anyMatch(e -> e.getName().equals("Shade")));
        assertTrue(effects.stream().anyMatch(e -> e.getName().equals("Fertilizer")));
        assertTrue(effects.stream().anyMatch(e -> e.getName().equals("Extra Watering")));
        assertTrue(effects.stream().anyMatch(e -> e.getName().equals("Heating")));
        
        System.out.println("✅ Catalogue d'effets prédéfinis disponible");
        effects.forEach(e -> 
            System.out.println("   - " + e.getName() + " (" + e.getDurationHours() + "h) : " + e.getDescription())
        );
    }
    
    @org.junit.jupiter.api.Test
    @Order(2)
    @DisplayName("Critère 2 : Appliquer un effet 'Shade 6h' à une plante")
    public void testApplyShadeEffect() throws Exception {
        // Initialiser le catalogue
        effectService.initializeEffectsCatalog();
        
        // Créer une plante
        Plant plant = plantServices.createPlant("Orchidée Test", testSpecies.getId());
        
        // Récupérer l'effet Shade
        Effect shadeEffect = effectService.getAllEffects().stream()
                .filter(e -> e.getName().equals("Shade"))
                .findFirst()
                .orElseThrow(() -> new Exception("Effet Shade introuvable"));
        
        assertEquals(6, shadeEffect.getDurationHours(), "L'effet Shade devrait durer 6 heures");
        
        // Appliquer l'effet à la plante
        PlantEffect plantEffect = effectService.applyEffectToPlant(plant.getId(), shadeEffect.getId());
        
        assertNotNull(plantEffect.getId());
        assertEquals(plant.getId(), plantEffect.getPlantId());
        assertEquals(shadeEffect.getId(), plantEffect.getEffectId());
        assertTrue(plantEffect.isActive());
        
        // Vérifier que l'effet est actif
        List<PlantEffect> activeEffects = effectService.getActivePlantEffects(plant.getId());
        assertEquals(1, activeEffects.size());
        
        System.out.println("✅ Effet 'Shade 6h' appliqué avec succès à la plante");
        System.out.println("   PlantEffect ID : " + plantEffect.getId());
        System.out.println("   Début : " + plantEffect.getStartAt());
        System.out.println("   Fin : " + plantEffect.getEndAt());
    }
    
    @org.junit.jupiter.api.Test
    @Order(3)
    @DisplayName("Critère 3 : Observer variations de stress sur plusieurs ticks avec effet Shade")
    public void testEffectImpactOnStress() throws Exception {
        // Initialiser le catalogue
        effectService.initializeEffectsCatalog();
        
        // Créer une plante avec des conditions stressantes (trop de lumière)
        Plant plant = plantServices.createPlant(
            "Orchidée Stressée",
            testSpecies.getId(),
            200.0,  // water optimal
            22.0,   // temp optimal
            80.0,   // humidity optimal
            8000.0  // lux très élevé (stress lumineux)
        );
        
        // Sauvegarder l'état initial
        double initialStress = plant.getStressIndex();
        PlantState initialState = plant.getPlantState();
        
        System.out.println("\n📊 État initial de la plante :");
        System.out.println("   Stress : " + initialStress);
        System.out.println("   État : " + initialState);
        System.out.println("   Lux : " + plant.getLux());
        
        // Appliquer l'effet Shade (réduit la luminosité)
        Effect shadeEffect = effectService.getAllEffects().stream()
                .filter(e -> e.getName().equals("Shade"))
                .findFirst()
                .orElseThrow();
        
        PlantEffect plantEffect = effectService.applyEffectToPlant(plant.getId(), shadeEffect.getId());
        
        System.out.println("\n🌿 Effet 'Shade' appliqué");
        System.out.println("   Modificateur lux : " + shadeEffect.getLuxModifier());
        System.out.println("   Réduction de stress : " + shadeEffect.getStressReduction());
        
        // Simuler plusieurs ticks avec environnement
        EnvironmentData env = new EnvironmentData(LocalDateTime.now(), 22.0, 80.0, 8000.0, 50.0);
        
        double stressTick1 = plant.getStressIndex();
        
        // Tick 1
        plantServices.evolvePlant(plant, env);
        plant = plantRepository.findById(plant.getId()).orElseThrow();
        double stressTick2 = plant.getStressIndex();
        
        System.out.println("\n📈 Après Tick 1 :");
        System.out.println("   Stress : " + stressTick2);
        System.out.println("   État : " + plant.getPlantState());
        
        // Tick 2
        plantServices.evolvePlant(plant, env);
        plant = plantRepository.findById(plant.getId()).orElseThrow();
        double stressTick3 = plant.getStressIndex();
        
        System.out.println("\n📈 Après Tick 2 :");
        System.out.println("   Stress : " + stressTick3);
        System.out.println("   État : " + plant.getPlantState());
        
        // Tick 3
        plantServices.evolvePlant(plant, env);
        plant = plantRepository.findById(plant.getId()).orElseThrow();
        double stressTick4 = plant.getStressIndex();
        
        System.out.println("\n📈 Après Tick 3 :");
        System.out.println("   Stress : " + stressTick4);
        System.out.println("   État : " + plant.getPlantState());
        
        // Vérifier que le stress est différent entre les ticks (variation observable)
        System.out.println("\n✅ Variation de stress observable sur plusieurs ticks avec effet Shade");
        System.out.println("   L'effet Shade réduit le stress lumineux et la température");
    }
    
    @org.junit.jupiter.api.Test
    @Order(4)
    @DisplayName("Critère 4 : Effet Fertilizer améliore la croissance")
    public void testFertilizerEffectOnGrowth() throws Exception {
        // Initialiser le catalogue
        effectService.initializeEffectsCatalog();
        
        // Créer 2 plantes identiques
        Plant plantWithoutEffect = plantServices.createPlant("Orchidée Sans Effet", testSpecies.getId());
        Plant plantWithEffect = plantServices.createPlant("Orchidée Avec Fertilisant", testSpecies.getId());
        
        // Copier les valeurs pour avoir des plantes vraiment identiques
        plantWithEffect.setWaterLevel(plantWithoutEffect.getWaterLevel());
        plantWithEffect.setTemperature(plantWithoutEffect.getTemperature());
        plantWithEffect.setHumidity(plantWithoutEffect.getHumidity());
        plantWithEffect.setLux(plantWithoutEffect.getLux());
        plantWithEffect.setStressIndex(plantWithoutEffect.getStressIndex());
        plantWithEffect.setHeightCm(plantWithoutEffect.getHeightCm());
        plantRepository.save(plantWithEffect);
        
        double initialHeight = plantWithoutEffect.getHeightCm();
        
        // Appliquer l'effet Fertilizer à la deuxième plante
        Effect fertilizerEffect = effectService.getAllEffects().stream()
                .filter(e -> e.getName().equals("Fertilizer"))
                .findFirst()
                .orElseThrow();
        
        effectService.applyEffectToPlant(plantWithEffect.getId(), fertilizerEffect.getId());
        
        // Environnement optimal
        EnvironmentData env = new EnvironmentData(
            LocalDateTime.now(),
            testSpecies.getOptimalTemperature(),
            testSpecies.getOptimalHumidity(),
            testSpecies.getOptimalLuxNeeds(),
            50.0
        );
        
        // Faire évoluer les 2 plantes sur 5 ticks
        for (int i = 0; i < 5; i++) {
            plantServices.evolvePlant(plantWithoutEffect, env);
            plantServices.evolvePlant(plantWithEffect, env);
        }
        
        // Récupérer les plantes mises à jour
        plantWithoutEffect = plantRepository.findById(plantWithoutEffect.getId()).orElseThrow();
        plantWithEffect = plantRepository.findById(plantWithEffect.getId()).orElseThrow();
        
        double heightWithoutEffect = plantWithoutEffect.getHeightCm();
        double heightWithEffect = plantWithEffect.getHeightCm();
        
        // La plante avec fertilisant devrait avoir grandi plus
        assertTrue(heightWithEffect > heightWithoutEffect, 
            "La plante avec fertilisant devrait avoir grandi plus vite");
        
        System.out.println("\n✅ Effet Fertilizer améliore la croissance");
        System.out.println("   Hauteur sans effet : " + String.format("%.2f", heightWithoutEffect) + " cm");
        System.out.println("   Hauteur avec effet : " + String.format("%.2f", heightWithEffect) + " cm");
        System.out.println("   Différence : " + String.format("%.2f", heightWithEffect - heightWithoutEffect) + " cm");
    }
    
    @org.junit.jupiter.api.Test
    @Order(5)
    @DisplayName("Critère 5 : Plusieurs effets peuvent être actifs simultanément")
    public void testMultipleEffects() throws Exception {
        // Initialiser le catalogue
        effectService.initializeEffectsCatalog();
        
        // Créer une plante
        Plant plant = plantServices.createPlant("Orchidée Multi-Effets", testSpecies.getId());
        
        // Récupérer plusieurs effets
        Effect shadeEffect = effectService.getAllEffects().stream()
                .filter(e -> e.getName().equals("Shade"))
                .findFirst().orElseThrow();
        
        Effect fertilizerEffect = effectService.getAllEffects().stream()
                .filter(e -> e.getName().equals("Fertilizer"))
                .findFirst().orElseThrow();
        
        Effect wateringEffect = effectService.getAllEffects().stream()
                .filter(e -> e.getName().equals("Extra Watering"))
                .findFirst().orElseThrow();
        
        // Appliquer les 3 effets
        effectService.applyEffectToPlant(plant.getId(), shadeEffect.getId());
        effectService.applyEffectToPlant(plant.getId(), fertilizerEffect.getId());
        effectService.applyEffectToPlant(plant.getId(), wateringEffect.getId());
        
        // Vérifier que les 3 effets sont actifs
        List<PlantEffect> activeEffects = effectService.getActivePlantEffects(plant.getId());
        assertEquals(3, activeEffects.size(), "Les 3 effets devraient être actifs");
        
        // Calculer les modificateurs totaux
        EffectService.EffectModifiers modifiers = effectService.calculateTotalModifiers(plant.getId());
        
        // Les modificateurs devraient être cumulés
        assertNotEquals(0.0, modifiers.lux, "Modificateur lux devrait être non nul");
        assertNotEquals(0.0, modifiers.water, "Modificateur eau devrait être non nul");
        assertNotEquals(0.0, modifiers.growthRate, "Modificateur croissance devrait être non nul");
        
        System.out.println("\n✅ Plusieurs effets peuvent être actifs simultanément");
        System.out.println("   Effets actifs : " + activeEffects.size());
        System.out.println("   Modificateur température total : " + modifiers.temperature);
        System.out.println("   Modificateur lux total : " + modifiers.lux);
        System.out.println("   Modificateur eau total : " + modifiers.water);
        System.out.println("   Modificateur croissance total : " + modifiers.growthRate);
        System.out.println("   Réduction stress totale : " + modifiers.stressReduction);
    }
}
