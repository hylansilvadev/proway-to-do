package com.example.todo.model.dto;

import com.example.todo.model.enums.TaskStatus;

import java.time.LocalDateTime;

public class TaskResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TaskStatus taskStatus;
    private LocalDateTime createdAt;

    public TaskResponseDTO() {
    }

    public TaskResponseDTO(Long id, String title, String description, TaskStatus taskStatus, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskStatus = taskStatus;
        this.createdAt = createdAt;
    }

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

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
