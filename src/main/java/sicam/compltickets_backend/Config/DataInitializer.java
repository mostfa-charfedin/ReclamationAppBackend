package sicam.compltickets_backend.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sicam.compltickets_backend.Entities.User;
import sicam.compltickets_backend.Repositories.UserRepository;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initialisation des données...");

        // Créer un admin par défaut
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setNom("Administrateur");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("Admin créé: " + admin.getEmail());
        }

        // Créer un technicien par défaut
        if (userRepository.findByEmail("tech@example.com").isEmpty()) {
            User technicien = new User();
            technicien.setNom("Technicien Test");
            technicien.setEmail("tech@example.com");
            technicien.setPassword(passwordEncoder.encode("tech123"));
            technicien.setRole("TECHNICIEN");
            userRepository.save(technicien);
            System.out.println("Technicien créé: " + technicien.getEmail());
        }

        // Créer un utilisateur normal par défaut
        if (userRepository.findByEmail("user@example.com").isEmpty()) {
            User user = new User();
            user.setNom("Utilisateur Test");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("USER");
            userRepository.save(user);
            System.out.println("Utilisateur créé: " + user.getEmail());
        }

        System.out.println("Initialisation des données terminée !");
    }
}