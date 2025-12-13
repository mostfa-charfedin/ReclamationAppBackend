package sicam.compltickets_backend.Services;



import java.util.List;
import java.util.Optional;

import sicam.compltickets_backend.DTO.CommentRequest;
import sicam.compltickets_backend.DTO.CreateReclamationRequest;
import sicam.compltickets_backend.DTO.ReclamationStats;
import sicam.compltickets_backend.DTO.StatusUpdateRequest;
import sicam.compltickets_backend.Entities.Reclamation;

public interface ReclamationService {

    // CRUD Operations
    List<Reclamation> getAllReclamations();
    Optional<Reclamation> getReclamationById(String id);
    Reclamation createReclamation(CreateReclamationRequest request);
    Reclamation updateReclamation(String id, Reclamation reclamation);
    void deleteReclamation(String id);

    // Business Operations
    List<Reclamation> getReclamationsByUser(String userId);
    List<Reclamation> getReclamationsByTechnicien(String technicienId);
    Reclamation assignTechniciens(String reclamationId, List<String> technicienIds);
    Reclamation updateStatus(String reclamationId, StatusUpdateRequest request);
    Reclamation addComment(String reclamationId, CommentRequest request);
    Reclamation updateCategory(String reclamationId, String categoryId);
    void removeCategory(String reclamationId);
    Reclamation togglePriorite(String reclamationId);

    // Archivage (soft delete)
    void archiveReclamation(String reclamationId);
    Reclamation unarchiveReclamation(String reclamationId);
    List<Reclamation> getArchivedReclamations();

    // Export reclamations (supports optional date range or all flag) to Excel (bytes)
    byte[] exportAllReclamationsToExcel(String from, String to, Boolean all) throws Exception;

    // Statistics
    ReclamationStats getStats();
}