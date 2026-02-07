package com.dylan.todoapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity // Tells JPA to map this class to a database table
@Table(name = "tasks") // Optional: names the table "tasks" instead of "task"
@Data // Lombok annotation to generate getters/setters
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment (1, 2, 3...)
    private Long id;

    @Column(nullable = false)
    private String description;

    private boolean completed = false;


}