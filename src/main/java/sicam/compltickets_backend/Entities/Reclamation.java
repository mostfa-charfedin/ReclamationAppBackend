package sicam.compltickets_backend.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "reclamations")
public class Reclamation {
    @Id
    private String id;
    private String titre;
    private String description;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDebutTraitement;
    private LocalDateTime dateResolution;
    private String statut;

    @DBRef
    private User user;

    @DBRef
    private List<User> techniciens;

    private List<Commentaire> commentaires ;

    // Constructeurs
    public Reclamation() {
        this.dateCreation = LocalDateTime.now();
        this.statut = "ASSIGNEE";
    }

    public Reclamation(String titre, String description, User user) {
        this();
        this.titre = titre;
        this.description = description;
        this.user = user;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateDebutTraitement() { return dateDebutTraitement; }
    public void setDateDebutTraitement(LocalDateTime dateDebutTraitement) { this.dateDebutTraitement = dateDebutTraitement; }

    public LocalDateTime getDateResolution() { return dateResolution; }
    public void setDateResolution(LocalDateTime dateResolution) { this.dateResolution = dateResolution; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<User> getTechniciens() { return techniciens; }
    public void setTechniciens(List<User> techniciens) { this.techniciens = techniciens; }

    public List<Commentaire> getCommentaires() { return commentaires; }
    public void setCommentaires(List<Commentaire> commentaires) { this.commentaires = commentaires; }
}