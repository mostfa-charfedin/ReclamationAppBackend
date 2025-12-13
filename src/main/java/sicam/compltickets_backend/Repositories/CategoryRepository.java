package sicam.compltickets_backend.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import sicam.compltickets_backend.Entities.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findByNom(String nom);
    List<Category> findByActive(boolean active);
}
