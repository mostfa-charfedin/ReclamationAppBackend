package sicam.compltickets_backend.DTO;

import java.time.LocalDateTime;

public class CategorieDTO {
    private String id;
    private String nom;
    private String description;
    private LocalDateTime dateCreation;
    private boolean active;

    // Constructeurs
    public CategorieDTO() {}

    public CategorieDTO(String id, String nom, String description, LocalDateTime dateCreation, boolean active) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateCreation = dateCreation;
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
