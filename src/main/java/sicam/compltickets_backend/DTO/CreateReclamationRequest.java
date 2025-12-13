package sicam.compltickets_backend.DTO;

public class CreateReclamationRequest {
    private String titre;
    private String description;
    private String userId;
    private String categorieId;
    private String category; // expected values: BLOQUANT, MAJEURE, MINEURE

    // Constructeurs
    public CreateReclamationRequest() {}

    public CreateReclamationRequest(String titre, String description, String userId) {
        this.titre = titre;
        this.description = description;
        this.userId = userId;
    }

    public CreateReclamationRequest(String titre, String description, String userId, String categorieId, String category) {
        this.titre = titre;
        this.description = description;
        this.userId = userId;
        this.categorieId = categorieId;
        this.category = category;
    }

    // Getters et Setters
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCategorieId() { return categorieId; }
    public void setCategorieId(String categorieId) { this.categorieId = categorieId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}