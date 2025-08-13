package com.example.demo.service;


import com.example.demo.model.Department;
import com.example.demo.repost.DepartmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

  
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
    
    public Optional<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }
 // Modifier un département
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Département introuvable avec id : " + id));

        department.setName(departmentDetails.getName());

        return departmentRepository.save(department);
    }
    public Department findById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

}
