package sicam.compltickets_backend.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sicam.compltickets_backend.DTO.CommentRequest;
import sicam.compltickets_backend.DTO.CreateReclamationRequest;
import sicam.compltickets_backend.DTO.ReclamationStats;
import sicam.compltickets_backend.DTO.StatusUpdateRequest;
import sicam.compltickets_backend.Entities.Reclamation;
import sicam.compltickets_backend.Services.ReclamationService;

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

    @PutMapping("/{id}/category/{categoryId}")
    public ResponseEntity<Reclamation> updateCategory(@PathVariable String id, @PathVariable String categoryId) {
        return ResponseEntity.ok(reclamationService.updateCategory(id, categoryId));
    }

    @DeleteMapping("/{id}/category")
    public ResponseEntity<Void> removeCategory(@PathVariable String id) {
        reclamationService.removeCategory(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/priorite")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reclamation> togglePriorite(@PathVariable String id) {
        return ResponseEntity.ok(reclamationService.togglePriorite(id));
    }

    // Archive (soft delete) instead of physical delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> archiveReclamation(@PathVariable String id) {
        reclamationService.archiveReclamation(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/unarchive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Reclamation> unarchiveReclamation(@PathVariable String id) {
        return ResponseEntity.ok(reclamationService.unarchiveReclamation(id));
    }

    @GetMapping("/archived")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reclamation>> getArchivedReclamations() {
        return ResponseEntity.ok(reclamationService.getArchivedReclamations());
    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportReclamationsExcel(
            @org.springframework.web.bind.annotation.RequestParam(required = false) String from,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String to,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Boolean all
    ) {
        try {
            byte[] content = reclamationService.exportAllReclamationsToExcel(from, to, all);
            // Build a descriptive filename based on params
            String filename = "reclamations_report";
            if (all != null && all) {
                filename = "reclamations_all";
            } else if (from != null && !from.isEmpty() && to != null && !to.isEmpty()) {
                // sanitize and use the provided dates
                String f = from.replaceAll("[^0-9-]", "");
                String t = to.replaceAll("[^0-9-]", "");
                filename = String.format("reclamations_%s_%s", f, t);
            } else if (from != null && !from.isEmpty()) {
                String f = from.replaceAll("[^0-9-]", "");
                filename = String.format("reclamations_from_%s", f);
            } else if (to != null && !to.isEmpty()) {
                String t = to.replaceAll("[^0-9-]", "");
                filename = String.format("reclamations_to_%s", t);
            }
            String contentDisposition = String.format("attachment; filename=\"%s.xlsx\"", filename);
            return ResponseEntity.ok()
                    .header("Content-Disposition", contentDisposition)
                    .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .body(content);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}