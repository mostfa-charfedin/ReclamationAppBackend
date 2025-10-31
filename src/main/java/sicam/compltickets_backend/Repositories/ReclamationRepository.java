package sicam.compltickets_backend.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sicam.compltickets_backend.Entities.Reclamation;


import java.util.List;

public interface ReclamationRepository extends MongoRepository<Reclamation, String> {
    List<Reclamation> findByUserId(String userId);
    

    List<Reclamation> findByTechniciensId(String technicienId);
    
    List<Reclamation> findByStatut(String statut);
    
    @Query("{$or: [{'user.nom': {$regex: ?0, $options: 'i'}}, {'titre': {$regex: ?0, $options: 'i'}}]}")
    List<Reclamation> searchReclamations(String keyword);
}