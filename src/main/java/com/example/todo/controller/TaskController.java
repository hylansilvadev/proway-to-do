package com.example.todo.controller;

import com.example.todo.model.dto.TaskRequestDTO;
import com.example.todo.model.dto.TaskResponseDTO;
import com.example.todo.model.enums.TaskStatus;
import com.example.todo.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAll() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponseDTO>> getByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskRequestDTO dto) {
        return ResponseEntity.ok(taskService.create(dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus taskStatus
    ) {
        return ResponseEntity.ok(taskService.updateStatus(id, taskStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
