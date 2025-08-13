package com.example.demo.service;

import com.example.demo.model.Task;
import com.example.demo.model.User;
import com.example.demo.repost.TaskRepository;
import com.example.demo.repost.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    // Créer ou sauvegarder une tâche (sans user)
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    // Créer une tâche liée à un utilisateur
    public Task saveTaskWithUser(Task task, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec id : " + userId));
        task.setUser(user);
        return taskRepository.save(task);
    }

    // Récupérer les tâches d'un utilisateur
   
    
    public List<Task> getTasksByUserId(Long userId) {
        System.out.println("🛠️ Service appelé avec userId = " + userId);
        List<Task> tasks = taskRepository.findByUserId(userId);
        System.out.println("📄 Tâches trouvées : " + tasks);
        return tasks;
    }


    // Supprimer une tâche
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    // Trouver une tâche par ID
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // Modifier une tâche
    public Task updateTask(Long id, Task taskDetails) {
        Task existing = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
        existing.setTitle(taskDetails.getTitle());
        existing.setDescription(taskDetails.getDescription());
        existing.setCompleted(taskDetails.isCompleted());
        return taskRepository.save(existing);
    }
    

    public Task modifyTask(Long id, Task updatedTask) {
        Task existing = taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tâche non trouvée"));
        existing.setTitle(updatedTask.getTitle());
        existing.setDescription(updatedTask.getDescription());
        existing.setCompleted(updatedTask.isCompleted());
        return taskRepository.save(existing);
    }
    
    @Transactional
    public void updateValidationStatus(List<Long> taskIds, boolean status) {
        Integer val = status ? 1 : 0;  // true => 1, false => 0

        for (Long id : taskIds) {
            Optional<Task> optTask = taskRepository.findById(id);
            if (optTask.isPresent()) {
                Task task = optTask.get();
                task.setValidee(val);  // set Integer instead of boolean
                taskRepository.save(task);
            }
        }
    }

    

}
