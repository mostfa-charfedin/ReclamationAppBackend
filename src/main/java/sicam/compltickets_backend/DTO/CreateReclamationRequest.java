package sicam.compltickets_backend.DTO;

public class CreateReclamationRequest {
    private String titre;
    private String description;
    private String userId;

    // Constructeurs
    public CreateReclamationRequest() {}

    public CreateReclamationRequest(String titre, String description, String userId) {
        this.titre = titre;
        this.description = description;
        this.userId = userId;
    }

    // Getters et Setters
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}