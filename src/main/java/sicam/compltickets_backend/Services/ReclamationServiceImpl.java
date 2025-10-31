package sicam.compltickets_backend.Services;

import org.springframework.stereotype.Service;
import sicam.compltickets_backend.DTO.CommentRequest;
import sicam.compltickets_backend.DTO.CreateReclamationRequest;
import sicam.compltickets_backend.DTO.ReclamationStats;
import sicam.compltickets_backend.DTO.StatusUpdateRequest;
import sicam.compltickets_backend.Entities.Commentaire;
import sicam.compltickets_backend.Entities.Reclamation;
import sicam.compltickets_backend.Entities.User;
import sicam.compltickets_backend.Repositories.ReclamationRepository;
import sicam.compltickets_backend.Repositories.UserRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReclamationServiceImpl implements ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final UserRepository userRepository;

    // ✅ Injection par constructeur
    public ReclamationServiceImpl(ReclamationRepository reclamationRepository, UserRepository userRepository) {
        this.reclamationRepository = reclamationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @Override
    public Optional<Reclamation> getReclamationById(String id) {
        return reclamationRepository.findById(id);
    }

    @Override
    public Reclamation createReclamation(CreateReclamationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + request.getUserId()));

        Reclamation reclamation = new Reclamation();
        reclamation.setTitre(request.getTitre());
        reclamation.setDescription(request.getDescription());
        reclamation.setUser(user);
        reclamation.setDateCreation(LocalDateTime.now());
        reclamation.setStatut("NOUVELLE");

        return reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation updateReclamation(String id, Reclamation reclamationDetails) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée: " + id));

        reclamation.setTitre(reclamationDetails.getTitre());
        reclamation.setDescription(reclamationDetails.getDescription());
        return reclamationRepository.save(reclamation);
    }

    @Override
    public void deleteReclamation(String id) {
        if (!reclamationRepository.existsById(id)) {
            throw new RuntimeException("Réclamation non trouvée: " + id);
        }
        reclamationRepository.deleteById(id);
    }

    @Override
    public List<Reclamation> getReclamationsByUser(String userId) {
        return reclamationRepository.findByUserId(userId);
    }

    @Override
    public List<Reclamation> getReclamationsByTechnicien(String technicienId) {
        return reclamationRepository.findByTechniciensId(technicienId);
    }

    @Override
    public Reclamation assignTechniciens(String reclamationId, List<String> technicienIds) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée: " + reclamationId));

        List<User> techniciens = userRepository.findAllById(technicienIds);
        reclamation.setTechniciens(techniciens);
        reclamation.setStatut("ASSIGNEE");

        return reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation updateStatus(String reclamationId, StatusUpdateRequest request) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée: " + reclamationId));

        reclamation.setStatut(request.getStatut());

        if ("EN_COURS".equals(request.getStatut()) && reclamation.getDateDebutTraitement() == null) {
            reclamation.setDateDebutTraitement(LocalDateTime.now());
        } else if ("RESOLUE".equals(request.getStatut()) && reclamation.getDateResolution() == null) {
            reclamation.setDateResolution(LocalDateTime.now());
        }

        return reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation addComment(String reclamationId, CommentRequest request) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée: " + reclamationId));

        User technicien = userRepository.findById(request.getTechnicienId())
                .orElseThrow(() -> new RuntimeException("Technicien non trouvé: " + request.getTechnicienId()));

        Commentaire commentaire = new Commentaire();
        commentaire.setCommentaire(request.getCommentaire());
        commentaire.setTechnicien(technicien);
        commentaire.setDate(LocalDateTime.now());
        if (reclamation.getCommentaires() == null) {
            reclamation.setCommentaires(new ArrayList<>());
        }
        reclamation.getCommentaires().add(commentaire);
        return reclamationRepository.save(reclamation);
    }

    @Override
    public ReclamationStats getStats() {
        List<Reclamation> allReclamations = reclamationRepository.findAll();

        long nouvelles = allReclamations.stream()
                .filter(r -> "NOUVELLE".equals(r.getStatut())).count();
        long assignees = allReclamations.stream()
                .filter(r -> "ASSIGNEE".equals(r.getStatut())).count();
        long enCours = allReclamations.stream()
                .filter(r -> "EN_COURS".equals(r.getStatut())).count();
        long resolues = allReclamations.stream()
                .filter(r -> "RESOLUE".equals(r.getStatut())).count();

        return new ReclamationStats(nouvelles, assignees, enCours, resolues, allReclamations.size());
    }
}