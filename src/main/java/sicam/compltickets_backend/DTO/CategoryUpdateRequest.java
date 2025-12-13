package sicam.compltickets_backend.DTO;

public class CategoryUpdateRequest {
    private String category; // Expected values: BLOQUANT, MAJEURE, MINEURE

    // Constructeurs
    public CategoryUpdateRequest() {}

    public CategoryUpdateRequest(String category) {
        this.category = category;
    }

    // Getters et Setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
