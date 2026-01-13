package org.example.entites;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "forests")
public class Forest {
    
    @Id
    private String id;
    
    @NotBlank(message = "Le nom de la forêt est obligatoire")
    private String name;
    
    @Min(value = 1, message = "La largeur doit être au moins 1")
    private int width;
    
    @Min(value = 1, message = "La hauteur doit être au moins 1")
    private int height;
    
    private LocalDateTime createdAt;
    
    // Option B : Forest contient cells pour stocker les positions des plantes
    private List<ForestCell> cells;
    
    // Constructeur
    public Forest(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.createdAt = LocalDateTime.now();
        this.cells = new ArrayList<>();
    }
    
    // Constructeur par défaut pour MongoDB
    protected Forest() {
        this.cells = new ArrayList<>();
    }
    
    // Méthode pour vérifier si une position est occupée
    public boolean isPositionOccupied(int x, int y) {
        return cells.stream()
                .anyMatch(cell -> cell.getX() == x && cell.getY() == y);
    }
    
    // Méthode pour obtenir une cellule à une position donnée
    public ForestCell getCellAt(int x, int y) {
        return cells.stream()
                .filter(cell -> cell.getX() == x && cell.getY() == y)
                .findFirst()
                .orElse(null);
    }
    
    // Méthode pour ajouter une cellule (plante à une position)
    public void addCell(ForestCell cell) {
        if (cell.getX() < 0 || cell.getX() >= width || cell.getY() < 0 || cell.getY() >= height) {
            throw new IllegalArgumentException("Position hors des limites de la forêt");
        }
        if (isPositionOccupied(cell.getX(), cell.getY())) {
            throw new IllegalArgumentException("Position déjà occupée");
        }
        cells.add(cell);
    }
    
    // Méthode pour retirer une plante d'une position
    public void removePlantAt(int x, int y) {
        cells.removeIf(cell -> cell.getX() == x && cell.getY() == y);
    }
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<ForestCell> getCells() {
        return cells;
    }
    
    public void setCells(List<ForestCell> cells) {
        this.cells = cells;
    }
    
    // Classe interne pour représenter une cellule de la forêt
    public static class ForestCell {
        private int x;
        private int y;
        private String plantId;
        
        public ForestCell(int x, int y, String plantId) {
            this.x = x;
            this.y = y;
            this.plantId = plantId;
        }
        
        // Constructeur par défaut
        public ForestCell() {}
        
        public int getX() {
            return x;
        }
        
        public void setX(int x) {
            this.x = x;
        }
        
        public int getY() {
            return y;
        }
        
        public void setY(int y) {
            this.y = y;
        }
        
        public String getPlantId() {
            return plantId;
        }
        
        public void setPlantId(String plantId) {
            this.plantId = plantId;
        }
    }
}
