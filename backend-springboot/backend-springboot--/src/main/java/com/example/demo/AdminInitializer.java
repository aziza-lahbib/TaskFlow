package com.example.demo;


import com.example.demo.model.User;
import com.example.demo.repost.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initAdmin() {
        if (userRepository.findAllByRole("ADMIN").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setRole("ADMIN");
            admin.setDepartment(null);
            admin.setTasks(new ArrayList<>());

            userRepository.save(admin);
            System.out.println("✅ Admin créé via @PostConstruct");
        } else {
            System.out.println("ℹ️ Admin déjà existant");
        }
    }
}

