package com.example.demo.controller;



import com.example.demo.model.Task;
import com.example.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Lister toutes les tâches d’un utilisateur
    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUser(@PathVariable Long userId) {
        System.out.println("🔍 Requête reçue pour lister les tâches de l'utilisateur ID = " + userId);
        List<Task> tasks = taskService.getTasksByUserId(userId);
        System.out.println("📦 Nombre de tâches récupérées : " + tasks.size());
        return tasks;
    }

    // Créer une tâche
    @PostMapping("/user/{userId}")
    public ResponseEntity<Task> createTask(@PathVariable Long userId, @RequestBody Task task) {
        Task savedTask = taskService.saveTaskWithUser(task, userId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedTask.getId())
            .toUri();
        return ResponseEntity.created(location).body(savedTask);
    }

    // Modifier une tâche
   
    
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task updated = taskService.updateTask(id, taskDetails);
        return ResponseEntity.ok(updated);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateTasks(@RequestBody List<Long> taskIds) {
        taskService.updateValidationStatus(taskIds, true);
        return ResponseEntity.ok("Tâches validées");
        
    }

    @PostMapping("/refuse")
    public ResponseEntity<String> refuseTasks(@RequestBody List<Long> taskIds) {
        taskService.updateValidationStatus(taskIds, false);
        return ResponseEntity.ok("Tâches refusées");
    }
    
    

}
