package com.example.demo.controller;


import com.example.demo.model.Department;
import com.example.demo.model.User;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
	private UserService userService;


    @Autowired
    private DepartmentService departmentService;

    // Lister tous les départements
    @GetMapping
    public List<Department> getAllDepartements() {
        return departmentService.getAllDepartments();
    }
    // Créer un département
    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.saveDepartment(department);
    }

    // Supprimer un département
    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }

    // Modifier un département
    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department departmentDetails) {
        return departmentService.updateDepartment(id, departmentDetails);
    }
    @GetMapping("/short")
    public List<Map<String, Object>> getDepartmentsShort() {
        List<Department> departments = departmentService.getAllDepartments();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Department dept : departments) {
            Map<String, Object> d = new HashMap<>();
            d.put("id", dept.getId());
            d.put("name", dept.getName());
            result.add(d);
        }

        return result;
    }

    @GetMapping("/{id}/users")
    public List<User> getUsersByDepartmentId(@PathVariable Long id) {
        Department dept = departmentService.findById(id);
        if (dept != null) {
            return dept.getUsers();
        } else {
            throw new RuntimeException("Département introuvable avec l'ID : " + id);
        }
    }


}
