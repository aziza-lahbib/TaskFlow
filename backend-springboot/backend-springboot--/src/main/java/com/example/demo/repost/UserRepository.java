package com.example.demo.repost;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
    Optional<User> findByRole(String role);

    List<User> findAllByRole(String role);
    List<User> findByDepartmentId(Long departmentId);

}
