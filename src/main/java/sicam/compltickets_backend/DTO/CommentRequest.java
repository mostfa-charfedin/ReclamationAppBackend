package sicam.compltickets_backend.DTO;

public class CommentRequest {
    private String commentaire;
    private String technicienId;

    // Constructeurs
    public CommentRequest() {}

    public CommentRequest(String commentaire, String technicienId) {
        this.commentaire = commentaire;
        this.technicienId = technicienId;
    }

    // Getters et Setters
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public String getTechnicienId() { return technicienId; }
    public void setTechnicienId(String technicienId) { this.technicienId = technicienId; }
}