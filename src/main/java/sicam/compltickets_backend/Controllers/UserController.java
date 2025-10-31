package sicam.compltickets_backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sicam.compltickets_backend.Entities.User;
import sicam.compltickets_backend.Services.UserService;


import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/techniciens")
    public List<User> getTechniciens() {
        return userService.getTechniciens();
    }

    @PostMapping("/techniciens")
    public User createTechnicien(@RequestBody User technicien) {
        return userService.createTechnicien(technicien);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}

