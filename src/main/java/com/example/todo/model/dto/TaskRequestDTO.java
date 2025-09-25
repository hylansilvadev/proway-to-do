package com.example.todo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 75, message = "Título deve estar entre 3 e 75 caracteres")
    private String title;

    @Size(max = 300, message = "Descrição não pode ser maior do que 300 caracteres")
    private String description;

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
