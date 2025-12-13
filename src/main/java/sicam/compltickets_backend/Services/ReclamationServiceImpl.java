package sicam.compltickets_backend.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sicam.compltickets_backend.DTO.CommentRequest;
import sicam.compltickets_backend.DTO.CreateReclamationRequest;
import sicam.compltickets_backend.DTO.ReclamationStats;
import sicam.compltickets_backend.DTO.StatusUpdateRequest;
import sicam.compltickets_backend.Entities.Category;
import sicam.compltickets_backend.Entities.Commentaire;
import sicam.compltickets_backend.Entities.Reclamation;
import sicam.compltickets_backend.Entities.User;
import sicam.compltickets_backend.Repositories.CategoryRepository;
import sicam.compltickets_backend.Repositories.ReclamationRepository;
import sicam.compltickets_backend.Repositories.UserRepository;

@Service
public class ReclamationServiceImpl implements ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // ✅ Injection par constructeur
    public ReclamationServiceImpl(ReclamationRepository reclamationRepository, 
                                 UserRepository userRepository,
                                 CategoryRepository categoryRepository) {
        this.reclamationRepository = reclamationRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Reclamation> getAllReclamations() {
        // Debug: fetch total count and filtered count to understand missing items
        List<Reclamation> all = reclamationRepository.findAll();
        List<Reclamation> reclamations = reclamationRepository.findByArchivedFalse();
        System.out.println("[DEBUG] ReclamationServiceImpl.getAllReclamations - total in DB: " + (all == null ? 0 : all.size()) + ", returned by findByArchivedFalse: " + (reclamations == null ? 0 : reclamations.size()));
        // If the filtered list is empty but DB has items, return all for debugging (temporary)
        if ((reclamations == null || reclamations.isEmpty()) && all != null && !all.isEmpty()) {
            System.out.println("[DEBUG] findByArchivedFalse returned empty but DB has items — returning all for debug purposes");
            // sort the full list and return
            all.sort((r1, r2) -> Boolean.compare(r2.isPriorite(), r1.isPriorite()));
            return all;
        }

        // Trier pour mettre les réclamations prioritaires en tête
        reclamations.sort((r1, r2) -> Boolean.compare(r2.isPriorite(), r1.isPriorite()));
        return reclamations;
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

        // Set category if provided
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            Category category = categoryRepository.findById(request.getCategory())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + request.getCategory()));
            reclamation.setCategory(category);
        }

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
        Reclamation reclamation = reclamationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Réclamation non trouvée: " + id));
        reclamation.setArchived(true);
        reclamation.setArchivedAt(LocalDateTime.now());
        // Try to set archivedBy from SecurityContext if available
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof sicam.compltickets_backend.Entities.User) {
                sicam.compltickets_backend.Entities.User current = (sicam.compltickets_backend.Entities.User) auth.getPrincipal();
                reclamation.setArchivedBy(current.getId());
                String name = (current.getPrenom() != null ? current.getPrenom() : "") + (current.getNom() != null ? " " + current.getNom() : "");
                if (name.trim().isEmpty()) name = current.getEmail();
                reclamation.setArchivedByName(name);
            }
        } catch (Exception ex) {
            // ignore - security may not be configured
        }
        reclamationRepository.save(reclamation);
    }

    @Override
    public List<Reclamation> getReclamationsByUser(String userId) {
        List<Reclamation> reclamations = reclamationRepository.findByUserIdAndArchivedFalse(userId);
        // Trier pour mettre les réclamations prioritaires en tête
        reclamations.sort((r1, r2) -> Boolean.compare(r2.isPriorite(), r1.isPriorite()));
        return reclamations;
    }

    @Override
    public List<Reclamation> getReclamationsByTechnicien(String technicienId) {
        System.out.println("[DEBUG] ReclamationServiceImpl.getReclamationsByTechnicien - technicienId: " + technicienId);
        List<Reclamation> reclamations = reclamationRepository.findByTechniciensIdAndArchivedFalse(technicienId);
        System.out.println("[DEBUG] findByTechniciensIdAndArchivedFalse returned: " + (reclamations == null ? 0 : reclamations.size()));
        if ((reclamations == null || reclamations.isEmpty())) {
            List<Reclamation> all = reclamationRepository.findAll();
            System.out.println("[DEBUG] Total reclamations in DB: " + (all == null ? 0 : all.size()));
            if (all != null) {
                for (Reclamation r : all) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("[DEBUG] Reclamation id=").append(r.getId()).append(" techniciens=");
                    if (r.getTechniciens() != null) {
                        for (User t : r.getTechniciens()) {
                            sb.append(t.getId()).append(",");
                        }
                    } else {
                        sb.append("null");
                    }
                    System.out.println(sb.toString());
                }
            }
        }

        // Trier pour mettre les réclamations prioritaires en tête
        if (reclamations != null) {
            reclamations.sort((r1, r2) -> Boolean.compare(r2.isPriorite(), r1.isPriorite()));
            return reclamations;
        }
        return List.of();
    }

    @Override
    public void archiveReclamation(String reclamationId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée: " + reclamationId));
        reclamation.setArchived(true);
        reclamation.setArchivedAt(LocalDateTime.now());
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof sicam.compltickets_backend.Entities.User) {
                sicam.compltickets_backend.Entities.User current = (sicam.compltickets_backend.Entities.User) auth.getPrincipal();
                reclamation.setArchivedBy(current.getId());
                String name = (current.getPrenom() != null ? current.getPrenom() : "") + (current.getNom() != null ? " " + current.getNom() : "");
                if (name.trim().isEmpty()) name = current.getEmail();
                reclamation.setArchivedByName(name);
            }
        } catch (Exception ex) {
            // ignore
        }
        reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation unarchiveReclamation(String reclamationId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée: " + reclamationId));
        reclamation.setArchived(false);
        reclamation.setArchivedAt(null);
        reclamation.setArchivedBy(null);
        reclamation.setArchivedByName(null);
        return reclamationRepository.save(reclamation);
    }

    @Override
    public List<Reclamation> getArchivedReclamations() {
        List<Reclamation> reclamations = reclamationRepository.findByArchivedTrue();
        reclamations.sort((r1, r2) -> Boolean.compare(r2.isPriorite(), r1.isPriorite()));
        return reclamations;
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

    @Override
    public byte[] exportAllReclamationsToExcel(String from, String to, Boolean allFlag) throws Exception {
        // Collect reclamations depending on parameters
        List<Reclamation> all = reclamationRepository.findAll();

        List<Reclamation> toExport = new ArrayList<>();

        if (allFlag != null && allFlag) {
            toExport = all;
        } else if ((from != null && !from.isEmpty()) || (to != null && !to.isEmpty())) {
            // parse dates (expecting yyyy-MM-dd or ISO date). We'll use LocalDateTime boundaries.
            java.time.LocalDateTime fromDt = null;
            java.time.LocalDateTime toDt = null;
            try {
                if (from != null && !from.isEmpty()) {
                    java.time.LocalDate fd = java.time.LocalDate.parse(from);
                    fromDt = fd.atStartOfDay();
                }
                if (to != null && !to.isEmpty()) {
                    java.time.LocalDate td = java.time.LocalDate.parse(to);
                    toDt = td.atTime(23,59,59,999000000);
                }
            } catch (Exception ex) {
                // ignore parse errors and fallback to empty filter
                System.out.println("[WARN] exportAllReclamationsToExcel: invalid date params, exporting all");
                toExport = all;
            }

            if (toExport.isEmpty()) {
                for (Reclamation r : all) {
                    java.time.LocalDateTime dc = r.getDateCreation();
                    if (dc == null) continue;
                    boolean afterFrom = (fromDt == null) ? true : !dc.isBefore(fromDt);
                    boolean beforeTo = (toDt == null) ? true : !dc.isAfter(toDt);
                    if (afterFrom && beforeTo) {
                        toExport.add(r);
                    }
                }
            }
        } else {
            // default: export all
            toExport = all;
        }

        // Use Apache POI to create an Excel workbook
        org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Reclamations");

        // Header row
        org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
        String[] cols = new String[] {"ID", "Titre", "Description", "Utilisateur", "EmailUtilisateur", "DateCreation", "Statut", "Priorite", "Categorie", "Techniciens", "Archived", "ArchivedAt"};
        for (int c = 0; c < cols.length; c++) {
            org.apache.poi.ss.usermodel.Cell cell = header.createCell(c);
            cell.setCellValue(cols[c]);
        }

        int rowIdx = 1;
        for (Reclamation r : toExport) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowIdx++);
            int c = 0;
            row.createCell(c++).setCellValue(r.getId() == null ? "" : r.getId());
            row.createCell(c++).setCellValue(r.getTitre() == null ? "" : r.getTitre());
            row.createCell(c++).setCellValue(r.getDescription() == null ? "" : r.getDescription());
            String userName = "";
            String userEmail = "";
            if (r.getUser() != null) {
                userName = ((r.getUser().getPrenom() == null ? "" : r.getUser().getPrenom()) + " " + (r.getUser().getNom() == null ? "" : r.getUser().getNom())).trim();
                userEmail = r.getUser().getEmail() == null ? "" : r.getUser().getEmail();
            }
            row.createCell(c++).setCellValue(userName);
            row.createCell(c++).setCellValue(userEmail);
            row.createCell(c++).setCellValue(r.getDateCreation() == null ? "" : r.getDateCreation().toString());
            row.createCell(c++).setCellValue(r.getStatut() == null ? "" : r.getStatut());
            row.createCell(c++).setCellValue(r.isPriorite() ? "Oui" : "Non");
            row.createCell(c++).setCellValue(r.getCategory() == null ? "" : (r.getCategory().getNom() == null ? "" : r.getCategory().getNom()));
            // techniciens as semicolon separated
            if (r.getTechniciens() != null && !r.getTechniciens().isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (User t : r.getTechniciens()) {
                    if (t != null) {
                        if (sb.length() > 0) sb.append("; ");
                        sb.append((t.getPrenom() == null ? "" : t.getPrenom()) + " " + (t.getNom() == null ? "" : t.getNom()));
                    }
                }
                row.createCell(c++).setCellValue(sb.toString());
            } else {
                row.createCell(c++).setCellValue("");
            }
            row.createCell(c++).setCellValue(r.isArchived() ? "Oui" : "Non");
            row.createCell(c++).setCellValue(r.getArchivedAt() == null ? "" : r.getArchivedAt().toString());
        }

        // Autosize columns (limit to reasonable amount to avoid perf issues)
        for (int i = 0; i < cols.length; i++) {
            sheet.autoSizeColumn(i);
        }

        java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();
        return bos.toByteArray();
    }

    @Override
    public Reclamation updateCategory(String reclamationId, String categoryId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée: " + reclamationId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + categoryId));

        reclamation.setCategory(category);
        return reclamationRepository.save(reclamation);
    }

    @Override
    public void removeCategory(String reclamationId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée: " + reclamationId));

        reclamation.setCategory(null);
        reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation togglePriorite(String reclamationId) {
        Reclamation reclamation = reclamationRepository.findById(reclamationId)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée: " + reclamationId));

        reclamation.setPriorite(!reclamation.isPriorite());
        return reclamationRepository.save(reclamation);
    }
}