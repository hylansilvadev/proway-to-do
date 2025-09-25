package com.example.todo.service;

import com.example.todo.exception.ResourceNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.model.dto.TaskRequestDTO;
import com.example.todo.model.dto.TaskResponseDTO;
import com.example.todo.model.enums.TaskStatus;
import com.example.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa Não Encontrada Para o Id: " + id));
        return toDTO(task);
    }

    public List<TaskResponseDTO> findByStatus(TaskStatus taskStatus) {
        return taskRepository.findByTaskStatus(taskStatus)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TaskResponseDTO create(TaskRequestDTO taskRequestDTO) {
        Task task = toEntity(taskRequestDTO);
        task.setStatus(TaskStatus.PENDING);
        return toDTO(taskRepository.save(task));
    }

    public TaskResponseDTO updateStatus(Long id, TaskStatus taskStatus) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa Não Encontrada Para o Id: " + id));
        task.setStatus(taskStatus);
        return toDTO(taskRepository.save(task));
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarefa Não Encontrada Para o Id: " + id);
        }
        taskRepository.deleteById(id);
    }

    private Task toEntity(TaskRequestDTO taskRequestDTO) {
        Task task = new Task();
        task.setTitle(taskRequestDTO.getTitle());
        task.setDescription(taskRequestDTO.getDescription());
        return task;
    }

    private TaskResponseDTO toDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setTaskStatus(task.getStatus());
        dto.setCreatedAt(task.getCreatedAt());
        return dto;
    }
}
