package org.example.services;

import org.example.entites.Forest;
import org.example.entites.Forest.ForestCell;
import org.example.entites.Plant;
import org.example.repositories.ForestRepository;
import org.example.repositories.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ForestService {
    
    @Autowired
    private ForestRepository forestRepository;
    
    @Autowired
    private PlantRepository plantRepository;
    
    /**
     * Crée une nouvelle forêt.
     */
    public Forest createForest(String name, int width, int height) {
        Forest forest = new Forest(name, width, height);
        return forestRepository.save(forest);
    }
    
    /**
     * Récupère toutes les forêts.
     */
    public List<Forest> getAllForests() {
        return forestRepository.findAll();
    }
    
    /**
     * Récupère une forêt par son ID.
     */
    public Optional<Forest> getForestById(String forestId) {
        return forestRepository.findById(forestId);
    }
    
    /**
     * Ajoute une plante à une forêt à une position (x, y).
     * 
     * R1 : Vérifie l'unicité de position (x,y) dans la forêt.
     * R2 : Vérifie la diversité (pas de clones exacts).
     * 
     * @throws IllegalArgumentException si position occupée ou plante clone
     * @throws Exception si forêt ou plante introuvable
     */
    /**
 * Ajoute une plante à une position précise dans une forêt.
 * 
 * @param forestId ID de la forêt
 * @param plantId  ID de la plante à placer
 * @param x        coordonnée x (colonne)
 * @param y        coordonnée y (ligne)
 * @return la forêt mise à jour
 * @throws Exception en cas d'erreur (position occupée, hors limite, plante/foret introuvable, etc.)
 */
public Forest addPlantToForest(@NonNull String forestId,@NonNull String plantId, int x, int y) throws Exception {
    // 1. Récupérer la forêt
    Forest forest = forestRepository.findById(forestId)
            .orElseThrow(() -> new Exception("Forêt introuvable : " + forestId));

    // 2. Récupérer la plante
    Plant plant = plantRepository.findById(plantId)
            .orElseThrow(() -> new Exception("Plante introuvable : " + plantId));

    // Debug : afficher l'état actuel des cellules
    System.out.println("DEBUG addPlantToForest - Forest " + forestId + " a " + (forest.getCells() != null ? forest.getCells().size() : 0) + " cellules avant ajout");

    if (forest.getCells() != null) {
        forest.getCells().forEach(cell -> 
            System.out.println("   → Cellule existante : (" + cell.getX() + "," + cell.getY() + ") plantId=" + cell.getPlantId()));
    }

    // 3. Vérifier que la liste cells existe (protection)
    if (forest.getCells() == null) {
        forest.setCells(new ArrayList<>());  // IMPORTANT : initialisation si null
        System.out.println("DEBUG : Liste cells était null → initialisée à vide");
    }

    // 4. R1 : Vérifier l'unicité de position
    if (forest.isPositionOccupied(x, y)) {
        String msg = String.format("Position (%d, %d) déjà occupée dans la forêt %s", x, y, forestId);
        System.out.println("CONFLIT : " + msg);
        throw new IllegalArgumentException(msg);
    }

    // 5. R2 : Vérifier la diversité (pas de clones exacts dans la même forêt)
    checkPlantDiversity(forest, plant);

    // 6. Vérifier les limites de la grille
    if (x < 0 || x >= forest.getWidth() || y < 0 || y >= forest.getHeight()) {
        throw new IllegalArgumentException(
            String.format("Position (%d, %d) hors limites (%dx%d)", x, y, forest.getWidth(), forest.getHeight())
        );
    }

    // 7. Créer et ajouter la cellule
    ForestCell cell = new ForestCell(x, y, plantId);
    forest.addCell(cell);

    // Debug : état après ajout
    System.out.println("DEBUG : cellule ajoutée → (" + x + "," + y + ") pour plant " + plantId);
    System.out.println("DEBUG : nombre de cellules APRÈS ajout = " + forest.getCells().size());

    // 8. Mettre à jour la plante (position + lien forêt)
    plant.setForestId(forestId);
    plant.setX(x);
    plant.setY(y);
    plantRepository.save(plant);

    // 9. Sauvegarder la forêt
    return forestRepository.save(forest);
}
    
    /**
     * R2 : Vérifie qu'il n'existe pas déjà une plante identique dans la forêt.
     * Deux plantes sont considérées comme des clones si :
     * - Même espèce (ID)
     * - Même variationSeed
     * - Caractéristiques trop similaires
     */
    private void checkPlantDiversity(Forest forest, Plant newPlant) throws IllegalArgumentException {
        // Récupérer toutes les plantes de la forêt
        List<Plant> plantsInForest = getPlantsInForest(forest.getId());
        
        for (Plant existingPlant : plantsInForest) {
            // Vérifier si c'est la même espèce
            if (existingPlant.getSpecies().getId().equals(newPlant.getSpecies().getId())) {
                // Si même espèce et même variationSeed : clone interdit
                if (existingPlant.getVariationSeed() == newPlant.getVariationSeed()) {
                    throw new IllegalArgumentException(
                        "Une plante identique (même espèce et même variationSeed) existe déjà dans cette forêt. " +
                        "Les plantes doivent être diversifiées (R2)."
                    );
                }
                
                // Vérification supplémentaire : caractéristiques trop similaires
                if (arePlantsTooCimilar(existingPlant, newPlant)) {
                    throw new IllegalArgumentException(
                        "Une plante avec des caractéristiques trop similaires existe déjà dans cette forêt. " +
                        "Les plantes doivent être diversifiées (R2)."
                    );
                }
            }
        }
    }
    
    /**
     * Vérifie si deux plantes ont des caractéristiques trop similaires.
     */
    private boolean arePlantsTooCimilar(Plant plant1, Plant plant2) {
        double threshold = 0.01; // Seuil de similarité (1%)
        
        boolean waterSimilar = Math.abs(plant1.getWaterLevel() - plant2.getWaterLevel()) < threshold * plant1.getWaterLevel();
        boolean tempSimilar = Math.abs(plant1.getTemperature() - plant2.getTemperature()) < threshold * 100;
        boolean humiditySimilar = Math.abs(plant1.getHumidity() - plant2.getHumidity()) < threshold * 100;
        boolean luxSimilar = Math.abs(plant1.getLux() - plant2.getLux()) < threshold * plant1.getLux();
        
        // Si toutes les caractéristiques sont similaires, considérer comme clone
        return waterSimilar && tempSimilar && humiditySimilar && luxSimilar;
    }
    
    /**
     * Récupère toutes les plantes d'une forêt.
     */
    public List<Plant> getPlantsInForest(@NonNull String forestId) {
        Forest forest = forestRepository.findById(forestId).orElse(null);
        if (forest == null) {
            return List.of();
        }
        
        // Récupérer les IDs des plantes depuis les cellules
        List<String> plantIds = forest.getCells().stream()
                .map(ForestCell::getPlantId)
                .collect(Collectors.toList());
        
        // Récupérer les plantes
        return plantRepository.findAllById(plantIds);
    }
    
    /**
     * Retire une plante d'une forêt.
     */
    public void removePlantFromForest(@NonNull String forestId, int x, int y) {
    Forest forest = forestRepository.findById(forestId)
            .orElseThrow(() -> new RuntimeException("Forêt non trouvée"));

    Optional<ForestCell> cellOpt = forest.getCellAt(x, y);

    if (cellOpt.isPresent()) {
        ForestCell cell = cellOpt.get();
        String plantId = cell.getPlantId();

        // Supprime la cellule
        forest.removeCellAt(x, y);

        // Optionnel : nettoie aussi la plante si nécessaire
        if (plantId != null) {
            plantRepository.findById(plantId).ifPresent(plant -> {
                plant.setForestId(null);
                plant.setX(null);
                plant.setY(null);
                plantRepository.save(plant);
            });
        }

        forestRepository.save(forest);
    }
    // else : rien à faire si pas de cellule à cette position
}
    
    /**
     * Supprime une forêt.
     */
    public void deleteForest(@NonNull String forestId) throws Exception {
        Forest forest = forestRepository.findById(forestId)
                .orElseThrow(() -> new Exception("Forêt introuvable : " + forestId));
        
        // Nettoyer les références dans les plantes
        List<Plant> plantsInForest = getPlantsInForest(forestId);
        for (Plant plant : plantsInForest) {
            plant.setForestId(null);
            plant.setX(null);
            plant.setY(null);
            plantRepository.save(plant);
        }
        
        forestRepository.delete(forest);
    }
}
