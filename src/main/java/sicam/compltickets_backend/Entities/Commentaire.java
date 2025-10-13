package sicam.compltickets_backend.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

public class Commentaire {
    @Id
    private String id;
    private String commentaire;
    private LocalDateTime date;

    @DBRef
    private User technicien;

    // Constructeurs
    public Commentaire() {
        this.date = LocalDateTime.now();
    }

    public Commentaire(String commentaire, User technicien) {
        this();
        this.commentaire = commentaire;
        this.technicien = technicien;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public User getTechnicien() { return technicien; }
    public void setTechnicien(User technicien) { this.technicien = technicien; }
}