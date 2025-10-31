package sicam.compltickets_backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sicam.compltickets_backend.DTO.AuthRequest;
import sicam.compltickets_backend.DTO.AuthResponse;
import sicam.compltickets_backend.DTO.RegisterRequest;
import sicam.compltickets_backend.Services.AuthService;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);

        if (response.getToken() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);

        if (response.getToken() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<AuthResponse> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            var userOpt = authService.validateToken(jwt);

            if (userOpt.isPresent()) {
                return ResponseEntity.ok(new AuthResponse(jwt, userOpt.get(), "Token valide"));
            }
        }
        return ResponseEntity.status(401).body(new AuthResponse(null, null, "Token invalide"));
    }
}