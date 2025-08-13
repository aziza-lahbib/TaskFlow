package com.example.demo.repost;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);

}
