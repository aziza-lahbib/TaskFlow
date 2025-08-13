package com.example.demo.controller;
import org.springframework.security.core.Authentication;

import com.example.demo.model.Department;
import com.example.demo.model.User;
import com.example.demo.repost.UserRepository;
import com.example.demo.repost.DepartmentRepository;

import com.example.demo.security.JwtUtil;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DepartmentService departmentService;

    @Autowired
    private UserService userService;
   
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;



    // 🔍 Trouver utilisateur par username
    @GetMapping("/{username}")
    public ResponseEntity<Optional<User>> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    // 📋 Lister tous les utilisateurs (idéal pour admin)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 🔍 Trouver utilisateur par ID
    @GetMapping("/id/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // ✏️ Modifier un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    // 🗑️ Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        System.out.println("🔔 Requête login reçue");

        String username = loginData.get("username");
        String password = loginData.get("password");

        System.out.println("📥 username reçu : " + username);
        System.out.println("📥 password reçu : " + password);

        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            System.out.println("❌ Utilisateur non trouvé");
            return ResponseEntity.status(404).body("Utilisateur non trouvé !");
        }

        User user = userOpt.get();

        boolean matches = passwordEncoder.matches(password, user.getPassword());

        System.out.println("🔐 Mot de passe hashé dans la base : " + user.getPassword());
        System.out.println("🔍 Le mot de passe correspond-il ? " + matches);

        if (!matches) {
            System.out.println("❌ Mot de passe incorrect");
            return ResponseEntity.status(401).body("Mot de passe incorrect !");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        System.out.println("✅ Token JWT généré : " + token);

        Map<String, Object> response = Map.of(
            "message", "Connexion réussie !",
            "username", user.getUsername(),
            "id", user.getId(),
            "token", token
        );

        return ResponseEntity.ok(response);
    }




   


    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Non authentifié");
        }

        String username = (String) authentication.getPrincipal();
        Optional<User> userOpt = userService.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé");
        }

        return ResponseEntity.ok(userOpt.get());
    }

    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        String email = (String) request.get("email");
        Long departmentId = Long.valueOf(request.get("departmentId").toString());

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Nom d'utilisateur déjà utilisé !");
        }

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // 🔒 encodage ici
        user.setEmail(email);
        user.setDepartment(department);
        user.setRole("USER");

        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Utilisateur enregistré avec succès !");
        return ResponseEntity.ok(response);    }
    
    
   

    
}
