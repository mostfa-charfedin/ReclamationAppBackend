package sicam.compltickets_backend.Entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
public class Category {
    @Id
    private String id;
    private String nom; // BLOQUANT, MAJEURE, MINEURE, ou custom
    private String description;
    private LocalDateTime dateCreation;
    private boolean active;

    // Constructeurs
    public Category() {
        this.dateCreation = LocalDateTime.now();
        this.active = true;
    }

    public Category(String nom, String description) {
        this();
        this.nom = nom;
        this.description = description;
    }

    public Category(String nom, String description, boolean active) {
        this();
        this.nom = nom;
        this.description = description;
        this.active = active;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
