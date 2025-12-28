package org.example.repositories;

import org.example.entites.Plant;
import org.example.entites.PlantState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository amélioré pour l'entité Plant
 * Inspiré des bonnes pratiques du projet GreenDesk, adapté à MongoDB et à notre modèle
 */
@Repository
public interface PlantRepository extends MongoRepository<Plant, String> {

    /**
     * Trouver toutes les plantes d'une espèce (par nom ou par référence si tu ajoutes un SpeciesRepository)
     */
    List<Plant> findBySpeciesName(String speciesName);

    /**
     * Trouver les plantes par état actuel (ex: toutes les plantes STRESSED ou DISEASED)
     */
    List<Plant> findByPlantState(PlantState plantState);

    /**
     * Trouver les plantes en stress élevé (stressIndex > seuil)
     */
    List<Plant> findByStressIndexGreaterThan(double stressIndexThreshold);

    /**
     * Trouver les plantes avec un niveau d'eau trop bas
     */
    List<Plant> findByWaterLevelLessThan(double waterLevelThreshold);

    /**
     * Trouver les plantes trop exposées à la lumière
     */
    List<Plant> findByLuxGreaterThan(double luxThreshold);

    /**
     * Plantes triées par stress croissant (les plus en danger en premier)
     */
    List<Plant> findAllByOrderByStressIndexDesc();

    /**
     * Plantes triées par hauteur décroissante (les plus grandes d'abord)
     */
    List<Plant> findAllByOrderByHeightCmDesc();

    /**
     * Plantes triées par niveau d'eau croissant (celles qui ont le plus besoin d'arrosage en premier)
     */
    List<Plant> findAllByOrderByWaterLevelAsc();

    /**
     * Compter le nombre de plantes par état
     */
    long countByPlantState(PlantState plantState);

    /**
     * Compter le nombre de plantes par espèce
     */
    long countBySpeciesName(String speciesName);

    /**
     * Moyenne du stress par espèce (requête personnalisée MongoDB)
     */
    @Query("{ 'species.name' : ?0 }")
    List<Plant> findAllBySpeciesName(String speciesName);

    // Pour calculer la moyenne du stressIndex par espèce
    @Query(value = "{ 'species.name' : ?0 }", fields = "{ 'stressIndex' : 1 }")
    List<Plant> findStressIndexBySpeciesName(String speciesName);
    // Tu pourras ensuite faire la moyenne en service : plants.stream().mapToDouble(Plant::getStressIndex).average()
}