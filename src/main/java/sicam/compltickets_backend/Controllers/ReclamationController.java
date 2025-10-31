package sicam.compltickets_backend.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sicam.compltickets_backend.DTO.CommentRequest;
import sicam.compltickets_backend.DTO.CreateReclamationRequest;
import sicam.compltickets_backend.DTO.ReclamationStats;
import sicam.compltickets_backend.DTO.StatusUpdateRequest;
import sicam.compltickets_backend.Entities.Reclamation;
import sicam.compltickets_backend.Services.ReclamationService;


import java.util.List;

@RestController
@RequestMapping("/api/reclamations")
@CrossOrigin(origins = "http://localhost:4200")
public class ReclamationController {

    private final ReclamationService reclamationService;

    public ReclamationController(ReclamationService reclamationService) {
        this.reclamationService = reclamationService;
    }

    @GetMapping
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> getReclamationById(@PathVariable String id) {
        return reclamationService.getReclamationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reclamation> createReclamation(@RequestBody CreateReclamationRequest request) {
        return ResponseEntity.ok(reclamationService.createReclamation(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reclamation>> getReclamationsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(reclamationService.getReclamationsByUser(userId));
    }

    @GetMapping("/technicien/{technicienId}")
    public ResponseEntity<List<Reclamation>> getReclamationsByTechnicien(@PathVariable String technicienId) {
        return ResponseEntity.ok(reclamationService.getReclamationsByTechnicien(technicienId));
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Reclamation> assignTechniciens(@PathVariable String id, @RequestBody List<String> technicienIds) {
        return ResponseEntity.ok(reclamationService.assignTechniciens(id, technicienIds));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Reclamation> updateStatus(@PathVariable String id, @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(reclamationService.updateStatus(id, request));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Reclamation> addComment(@PathVariable String id, @RequestBody CommentRequest request) {
        return ResponseEntity.ok(reclamationService.addComment(id, request));
    }

    @GetMapping("/stats")
    public ResponseEntity<ReclamationStats> getStats() {
        return ResponseEntity.ok(reclamationService.getStats());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable String id) {
        reclamationService.deleteReclamation(id);
        return ResponseEntity.ok().build();
    }
}