package com.example.demo.service;
import com.example.demo.model.Department;
import com.example.demo.model.User;
import com.example.demo.repost.DepartmentRepository;
import com.example.demo.repost.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
	private DepartmentRepository departmentRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ➤ Injecte le bean défini dans SecurityConfig

    // Sauvegarder un utilisateur (création)
    
    
    public User saveUser(User user) {
    	 // chercher le nom du département
        String deptName = user.getDepartment().getName();

        Department department = departmentRepository.findByName(deptName)
            .orElseThrow(() -> new RuntimeException("Département introuvable : " + deptName));

        user.setDepartment(department); // associer le vrai objet Department

        // Encoder le mot de passe avant d’enregistrer
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    // Trouver utilisateur par username
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Lister tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Trouver utilisateur par id
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Mettre à jour un utilisateur
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec id : " + id));

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());

        // ➤ Encoder le nouveau mot de passe s’il est changé
        String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
        user.setPassword(encodedPassword);

        user.setDepartment(userDetails.getDepartment());
        // mettre à jour d'autres champs si nécessaire

        return userRepository.save(user);
    }

    // Supprimer un utilisateur
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
   

    public List<User> getUsersByDepartment(Long departmentId) {
        return userRepository.findByDepartmentId(departmentId);
    }

    public User save(User user) {
        return userRepository.save(user);
    }


}
