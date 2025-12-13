package sicam.compltickets_backend.Entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private boolean priorite = false; // Attribut pour la priorité
    private boolean archived = false; // Attribut pour l'archivage (soft delete)
    private java.time.LocalDateTime archivedAt;
    private String archivedBy;
    private String archivedByName;

    @DBRef
    private User user;

    @DBRef
    private List<User> techniciens;

    @DBRef
    private Category category; // Référence à une catégorie dynamique

    private List<Commentaire> commentaires ;

    // Constructeurs
    public Reclamation() {
        this.dateCreation = LocalDateTime.now();
        this.statut = "ASSIGNEE";
        this.category = null; // Pas de catégorie par défaut (à assigner)
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

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public boolean isPriorite() { return priorite; }
    public void setPriorite(boolean priorite) { this.priorite = priorite; }

    public boolean isArchived() { return archived; }
    public void setArchived(boolean archived) { this.archived = archived; }

    public java.time.LocalDateTime getArchivedAt() { return archivedAt; }
    public void setArchivedAt(java.time.LocalDateTime archivedAt) { this.archivedAt = archivedAt; }
    public String getArchivedBy() { return archivedBy; }
    public void setArchivedBy(String archivedBy) { this.archivedBy = archivedBy; }
    public String getArchivedByName() { return archivedByName; }
    public void setArchivedByName(String archivedByName) { this.archivedByName = archivedByName; }
}