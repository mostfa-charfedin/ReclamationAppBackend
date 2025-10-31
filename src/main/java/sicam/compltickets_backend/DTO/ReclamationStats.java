package sicam.compltickets_backend.DTO;

public class ReclamationStats {
    private long nouvelles;
    private long assignees;
    private long enCours;
    private long resolues;
    private long total;

    // Constructeur
    public ReclamationStats(long nouvelles, long assignees, long enCours, long resolues, long total) {
        this.nouvelles = nouvelles;
        this.assignees = assignees;
        this.enCours = enCours;
        this.resolues = resolues;
        this.total = total;
    }

    // Getters seulement (immutable)
    public long getNouvelles() { return nouvelles; }
    public long getAssignees() { return assignees; }
    public long getEnCours() { return enCours; }
    public long getResolues() { return resolues; }
    public long getTotal() { return total; }
}