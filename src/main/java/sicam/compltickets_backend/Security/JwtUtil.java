package sicam.compltickets_backend.Security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "votre-secret-key-tres-longue-et-securisee-pour-jwt-gestion-reclamations-2024";
    private final long EXPIRATION_TIME = 86400000; // 24 heures

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    public String extractEmail(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            System.out.println("Erreur lors de l'extraction de l'email: " + e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("Token JWT invalide: " + e.getMessage());
            return false;
        }
    }

    // Méthode supplémentaire pour récupérer la date d'expiration
    public Date extractExpiration(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm)
                    .build()
                    .verify(token);
            return jwt.getExpiresAt();
        } catch (JWTVerificationException e) {
            System.out.println("Erreur lors de l'extraction de l'expiration: " + e.getMessage());
            return null;
        }
    }
}