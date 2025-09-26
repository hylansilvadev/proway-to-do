package com.example.todo.domain.swagger;

import com.example.todo.model.dto.TaskRequestDTO;
import com.example.todo.model.dto.TaskResponseDTO;
import com.example.todo.model.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(description = "Api que consulta, cadastra, atualiza e deleta tarefas", name = "Todo")
@RequestMapping("/tasks")
public interface ITaskSwagger {

    @Operation(summary = "Listar todas as tarefas", description = "Retorna uma lista com todas as tarefas cadastradas no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskResponseDTO.class))))
    })
    @GetMapping
    ResponseEntity<List<TaskResponseDTO>> getAll();

    @Operation(summary = "Buscar tarefa por ID", description = "Retorna uma tarefa específica pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @GetMapping("/{id}")
    ResponseEntity<TaskResponseDTO> getById(
            @Parameter(description = "ID da tarefa", required = true) @PathVariable Long id
    );

    @Operation(summary = "Buscar tarefas por status", description = "Retorna uma lista de tarefas filtradas pelo status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tarefas por status retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TaskResponseDTO.class))))
    })
    @GetMapping("/status/{status}")
    ResponseEntity<List<TaskResponseDTO>> getByStatus(
            @Parameter(description = "Status da tarefa", required = true) @PathVariable TaskStatus status
    );

    @Operation(summary = "Criar nova tarefa", description = "Cria uma nova tarefa no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    ResponseEntity<TaskResponseDTO> create(
            @Parameter(description = "Dados da nova tarefa", required = true)
            @Valid @RequestBody TaskRequestDTO dto
    );

    @Operation(summary = "Atualizar tarefa", description = "Atualiza o título e descrição de uma tarefa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PutMapping("/{id}")
    ResponseEntity<TaskResponseDTO> update(
            @Parameter(description = "ID da tarefa", required = true) @PathVariable Long id,
            @Parameter(description = "Novos dados da tarefa", required = true)
            @Valid @RequestBody TaskRequestDTO dto
    );

    @Operation(summary = "Atualizar status da tarefa", description = "Atualiza apenas o status de uma tarefa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da tarefa atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @PutMapping("/{id}/status")
    ResponseEntity<TaskResponseDTO> updateStatus(
            @Parameter(description = "ID da tarefa", required = true) @PathVariable Long id,
            @Parameter(description = "Novo status da tarefa", required = true) @RequestParam TaskStatus taskStatus
    );

    @Operation(summary = "Deletar tarefa", description = "Remove uma tarefa do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @Parameter(description = "ID da tarefa", required = true) @PathVariable Long id
    );
}
