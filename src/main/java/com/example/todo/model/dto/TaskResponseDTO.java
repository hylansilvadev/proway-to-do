package com.example.todo.model.dto;

import com.example.todo.model.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO de resposta contendo dados completos da tarefa")
public class TaskResponseDTO {

    @Schema(description = "ID único da tarefa", example = "1")
    private Long id;

    @Schema(description = "Título da tarefa", example = "Implementar API REST")
    private String title;

    @Schema(description = "Descrição detalhada da tarefa", example = "Implementar todas as operações CRUD da API de tarefas")
    private String description;

    @Schema(description = "Status atual da tarefa")
    private TaskStatus taskStatus;

    @Schema(description = "Data e hora de criação da tarefa", example = "2025-09-26T10:30:00")
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
