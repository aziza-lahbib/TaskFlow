package com.example.demo.repost;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
    

}
