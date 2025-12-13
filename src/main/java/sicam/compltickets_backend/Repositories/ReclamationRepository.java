package sicam.compltickets_backend.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sicam.compltickets_backend.Entities.Reclamation;


import java.util.List;

public interface ReclamationRepository extends MongoRepository<Reclamation, String> {
    List<Reclamation> findByUserId(String userId);

    @Query("{'userId': ?0, $or: [{'archived': {$exists: false}}, {'archived': false}]}")
    List<Reclamation> findByUserIdAndArchivedFalse(String userId);

    List<Reclamation> findByTechniciensId(String technicienId);

    // If `techniciens` are stored as embedded documents (List<User>), query by 'techniciens.id'
    // (previously used 'techniciens.$id' which expects DBRef and returned no results).
    @Query("{'techniciens.id': ?0, $or: [{'archived': {$exists: false}}, {'archived': false}]}")
    List<Reclamation> findByTechniciensIdAndArchivedFalse(String technicienId);

    List<Reclamation> findByStatut(String statut);

    @Query("{$or: [{'archived': {$exists: false}}, {'archived': false}]}")
    List<Reclamation> findByArchivedFalse();

    @Query("{'archived': true}")
    List<Reclamation> findByArchivedTrue();

    @Query("{$or: [{'user.nom': {$regex: ?0, $options: 'i'}}, {'titre': {$regex: ?0, $options: 'i'}}]}")
    List<Reclamation> searchReclamations(String keyword);

    @Query("{$and: [{$or: [{'user.nom': {$regex: ?0, $options: 'i'}}, {'titre': {$regex: ?0, $options: 'i'}}]}, {'archived': true}]}")
    List<Reclamation> searchArchivedReclamations(String keyword);
}