package sicam.compltickets_backend.DTO;

public class StatusUpdateRequest {
    private String statut;

    // Constructeurs
    public StatusUpdateRequest() {}

    public StatusUpdateRequest(String statut) {
        this.statut = statut;
    }

    // Getters et Setters
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
}
