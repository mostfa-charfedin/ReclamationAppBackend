package sicam.compltickets_backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sicam.compltickets_backend.Entities.User;
import sicam.compltickets_backend.Repositories.UserRepository;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getTechniciens() {
        return userRepository.findTechniciens();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        return userRepository.save(user);
    }

    public User createTechnicien(User technicien) {
        technicien.setRole("TECHNICIEN");
        return createUser(technicien);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Mettre à jour les champs modifiables
        if (userDetails.getNom() != null) {
            user.setNom(userDetails.getNom());
        }
        if (userDetails.getPrenom() != null) {
            user.setPrenom(userDetails.getPrenom());
        }
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            // Vérifier si le nouvel email n'est pas déjà utilisé
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email déjà utilisé");
            }
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }

        return userRepository.save(user);
    }


    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        userRepository.delete(user);
    }


    public User activateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setActive(true);
        return userRepository.save(user);
    }


    public User deactivateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setActive(false);
        return userRepository.save(user);
    }

    public User updateUserRole(String id, String newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Valider le rôle
        if (!isValidRole(newRole)) {
            throw new RuntimeException("Rôle invalide");
        }

        user.setRole(newRole);
        return userRepository.save(user);
    }


    private boolean isValidRole(String role) {
        return role != null &&
                (role.equals("USER") || role.equals("TECHNICIEN") || role.equals("ADMIN"));
    }


    public User resetPassword(String id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}
