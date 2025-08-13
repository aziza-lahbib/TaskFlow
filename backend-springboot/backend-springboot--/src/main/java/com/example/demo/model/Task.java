package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private boolean completed;
    
    


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    // Constructeurs
    public Task() {
    }

    public Task(Long id, String title, String description, boolean completed, User user ,Integer validee) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.user = user;
        this.validee = null;

    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    @Column(name = "validee")
    private Integer validee; // null = pas encore validée

    public Integer getValidee() {
        return validee;
    }

    public void setValidee(Integer validee) {
        this.validee = validee;
    }
}
