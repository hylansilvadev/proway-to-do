package com.example.todo.service;

import com.example.todo.exception.ResourceNotFoundException;
import com.example.todo.model.Task;
import com.example.todo.model.dto.TaskRequestDTO;
import com.example.todo.model.dto.TaskResponseDTO;
import com.example.todo.model.enums.TaskStatus;
import com.example.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TaskService Unit Tests")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskRequestDTO taskRequestDTO;
    private List<Task> taskList;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());

        taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTitle("Test Task");
        taskRequestDTO.setDescription("Test Description");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Test Task 2");
        task2.setDescription("Test Description 2");
        task2.setStatus(TaskStatus.COMPLETED);
        task2.setCreatedAt(LocalDateTime.now());

        taskList = Arrays.asList(task, task2);
    }

    @Test
    @DisplayName("Should return all tasks successfully")
    void shouldReturnAllTasksSuccessfully() {
        // Given
        when(taskRepository.findAll()).thenReturn(taskList);

        // When
        List<TaskResponseDTO> result = taskService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Task");
        assertThat(result.get(1).getTitle()).isEqualTo("Test Task 2");
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID successfully")
    void shouldReturnTaskByIdSuccessfully() {
        // Given
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // When
        TaskResponseDTO result = taskService.findById(taskId);

        // Then
        assertThat(result.getId()).isEqualTo(taskId);
        assertThat(result.getTitle()).isEqualTo("Test Task");
        assertThat(result.getTaskStatus()).isEqualTo(TaskStatus.PENDING);
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when task not found by ID")
    void shouldThrowExceptionWhenTaskNotFoundById() {
        // Given
        Long taskId = 999L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.findById(taskId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tarefa Não Encontrada Para o Id: " + taskId);

        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Should return tasks by status successfully")
    void shouldReturnTasksByStatusSuccessfully() {
        // Given
        TaskStatus status = TaskStatus.PENDING;
        List<Task> pendingTasks = Arrays.asList(task);
        when(taskRepository.findByTaskStatus(status)).thenReturn(pendingTasks);

        // When
        List<TaskResponseDTO> result = taskService.findByStatus(status);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTaskStatus()).isEqualTo(TaskStatus.PENDING);
        verify(taskRepository, times(1)).findByTaskStatus(status);
    }

    @Test
    @DisplayName("Should create task successfully")
    void shouldCreateTaskSuccessfully() {
        // Given
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        TaskResponseDTO result = taskService.create(taskRequestDTO);

        // Then
        assertThat(result.getTitle()).isEqualTo("Test Task");
        assertThat(result.getDescription()).isEqualTo("Test Description");
        assertThat(result.getTaskStatus()).isEqualTo(TaskStatus.PENDING);

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should update task successfully")
    void shouldUpdateTaskSuccessfully() {
        // Given
        Long taskId = 1L;
        TaskRequestDTO updateDTO = new TaskRequestDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        TaskResponseDTO result = taskService.update(taskId, updateDTO);

        // Then
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getDescription()).isEqualTo("Updated Description");

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent task")
    void shouldThrowExceptionWhenUpdatingNonExistentTask() {
        // Given
        Long taskId = 999L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.update(taskId, taskRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tarefa Não Encontrada Para o Id: " + taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should update task status successfully")
    void shouldUpdateTaskStatusSuccessfully() {
        // Given
        Long taskId = 1L;
        TaskStatus newStatus = TaskStatus.COMPLETED;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        TaskResponseDTO result = taskService.updateStatus(taskId, newStatus);

        // Then
        assertThat(result.getTaskStatus()).isEqualTo(TaskStatus.COMPLETED);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when updating status of non-existent task")
    void shouldThrowExceptionWhenUpdatingStatusOfNonExistentTask() {
        // Given
        Long taskId = 999L;
        TaskStatus newStatus = TaskStatus.COMPLETED;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> taskService.updateStatus(taskId, newStatus))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tarefa Não Encontrada Para o Id: " + taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should delete task successfully")
    void shouldDeleteTaskSuccessfully() {
        // Given
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);

        // When
        taskService.delete(taskId);

        // Then
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent task")
    void shouldThrowExceptionWhenDeletingNonExistentTask() {
        // Given
        Long taskId = 999L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> taskService.delete(taskId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tarefa Não Encontrada Para o Id: " + taskId);

        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, never()).deleteById(taskId);
    }
}
