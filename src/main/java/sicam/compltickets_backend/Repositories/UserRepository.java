package sicam.compltickets_backend.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import sicam.compltickets_backend.Entities.User;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    boolean existsByEmail(String email);
    
    @Query("{'role': 'TECHNICIEN'}")
    List<User> findTechniciens();
}