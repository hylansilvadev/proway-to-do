package com.example.todo.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para requisição de criação/atualização de tarefa")
public class TaskRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 75, message = "Título deve estar entre 3 e 75 caracteres")
    @Schema(description = "Título da tarefa", example = "Implementar API REST", required = true)
    private String title;

    @Size(max = 300, message = "Descrição não pode ser maior do que 300 caracteres")
    @Schema(description = "Descrição detalhada da tarefa", example = "Implementar todas as operações CRUD da API de tarefas")
    private String description;

    public TaskRequestDTO() {
    }

    public TaskRequestDTO(String title, String description) {
        this.title = title;
        this.description = description;
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
}
