package com.example.todo.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status possíveis para uma tarefa")
public enum TaskStatus {
    @Schema(description = "Tarefa pendente/não iniciada")
    PENDING,

    @Schema(description = "Tarefa concluída")
    COMPLETED
}
