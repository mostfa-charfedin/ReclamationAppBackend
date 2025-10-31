package sicam.compltickets_backend.DTO;


import sicam.compltickets_backend.Entities.User;

public class AuthResponse {
    private String token;
    private User user;
    private String message;

    // Constructeurs
    public AuthResponse() {}

    public AuthResponse(String token, User user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    // Getters et Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}