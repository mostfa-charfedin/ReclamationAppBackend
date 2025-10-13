package sicam.compltickets_backend.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String nom;
    private String prenom;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String role; // USER, TECHNICIEN, ADMIN
    private boolean active = true;
    private LocalDateTime dateCreation = LocalDateTime.now();
    private List<String> reclamationIds;

    // No-argument constructor (REQUIRED)
    public User() {
    }

    // Constructor with parameters
    public User(String nom, String prenom, String email, String password, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public List<String> getReclamationIds() { return reclamationIds; }
    public void setReclamationIds(List<String> reclamationIds) { this.reclamationIds = reclamationIds; }
}