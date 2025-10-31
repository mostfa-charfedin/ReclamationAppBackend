package sicam.compltickets_backend.Services;



import sicam.compltickets_backend.DTO.CommentRequest;
import sicam.compltickets_backend.DTO.CreateReclamationRequest;
import sicam.compltickets_backend.DTO.ReclamationStats;
import sicam.compltickets_backend.DTO.StatusUpdateRequest;
import sicam.compltickets_backend.Entities.Reclamation;

import java.util.List;
import java.util.Optional;

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



    // Statistics
    ReclamationStats getStats();
}