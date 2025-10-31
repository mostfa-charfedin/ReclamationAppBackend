package sicam.compltickets_backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sicam.compltickets_backend.DTO.AuthRequest;
import sicam.compltickets_backend.DTO.AuthResponse;
import sicam.compltickets_backend.DTO.RegisterRequest;
import sicam.compltickets_backend.Entities.User;
import sicam.compltickets_backend.Repositories.UserRepository;
import sicam.compltickets_backend.Security.JwtUtil;


import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse(null, null, "Email déjà utilisé");
        }

        // Créer le nouvel utilisateur
        User user = new User();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setActive(true);

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getEmail());

        return new AuthResponse(token, savedUser, "Inscription réussie");
    }

    public AuthResponse login(AuthRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return new AuthResponse(null, null, "Email ou mot de passe incorrect");
        }

        User user = userOpt.get();

        if (!user.isActive()) {
            return new AuthResponse(null, null, "Compte désactivé");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(null, null, "Email ou mot de passe incorrect");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user, "Connexion réussie");
    }

    public Optional<User> validateToken(String token) {
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);
            return userRepository.findByEmail(email);
        }
        return Optional.empty();
    }
}