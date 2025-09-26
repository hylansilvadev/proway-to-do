package com.example.todo.controller;

import com.example.todo.model.dto.TaskRequestDTO;
import com.example.todo.model.dto.TaskResponseDTO;
import com.example.todo.model.enums.TaskStatus;
import com.example.todo.service.TaskService;
import com.example.todo.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@DisplayName("TaskController Integration Tests")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskResponseDTO taskResponseDTO;
    private TaskRequestDTO taskRequestDTO;
    private List<TaskResponseDTO> taskList;

    @BeforeEach
    void setUp() {
        taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setId(1L);
        taskResponseDTO.setTitle("Test Task");
        taskResponseDTO.setDescription("Test Description");
        taskResponseDTO.setTaskStatus(TaskStatus.PENDING);
        taskResponseDTO.setCreatedAt(LocalDateTime.now());

        taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTitle("Test Task");
        taskRequestDTO.setDescription("Test Description");

        TaskResponseDTO task2 = new TaskResponseDTO();
        task2.setId(2L);
        task2.setTitle("Test Task 2");
        task2.setDescription("Test Description 2");
        task2.setTaskStatus(TaskStatus.COMPLETED);
        task2.setCreatedAt(LocalDateTime.now());

        taskList = Arrays.asList(taskResponseDTO, task2);
    }

    @Test
    @DisplayName("GET /tasks - Should return all tasks")
    void shouldReturnAllTasks() throws Exception {
        // Given
        when(taskService.findAll()).thenReturn(taskList);

        // When & Then
        mockMvc.perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].taskStatus").value("PENDING"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Test Task 2"))
                .andExpect(jsonPath("$[1].taskStatus").value("COMPLETED"));

        verify(taskService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /tasks/{id} - Should return task by ID")
    void shouldReturnTaskById() throws Exception {
        // Given
        Long taskId = 1L;
        when(taskService.findById(taskId)).thenReturn(taskResponseDTO);

        // When & Then
        mockMvc.perform(get("/tasks/{id}", taskId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.taskStatus").value("PENDING"));

        verify(taskService, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("GET /tasks/{id} - Should return 404 when task not found")
    void shouldReturn404WhenTaskNotFound() throws Exception {
        // Given
        Long taskId = 999L;
        when(taskService.findById(taskId))
                .thenThrow(new ResourceNotFoundException("Tarefa Não Encontrada Para o Id: " + taskId));

        // When & Then
        mockMvc.perform(get("/tasks/{id}", taskId))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("GET /tasks/status/{status} - Should return tasks by status")
    void shouldReturnTasksByStatus() throws Exception {
        // Given
        TaskStatus status = TaskStatus.PENDING;
        List<TaskResponseDTO> pendingTasks = Arrays.asList(taskResponseDTO);
        when(taskService.findByStatus(status)).thenReturn(pendingTasks);

        // When & Then
        mockMvc.perform(get("/tasks/status/{status}", status))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].taskStatus").value("PENDING"));

        verify(taskService, times(1)).findByStatus(status);
    }

    @Test
    @DisplayName("POST /tasks - Should create new task")
    void shouldCreateNewTask() throws Exception {
        // Given
        when(taskService.create(any(TaskRequestDTO.class))).thenReturn(taskResponseDTO);

        // When & Then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.taskStatus").value("PENDING"));

        verify(taskService, times(1)).create(any(TaskRequestDTO.class));
    }

    @Test
    @DisplayName("POST /tasks - Should return 400 for invalid data")
    void shouldReturn400ForInvalidData() throws Exception {
        // Given - Invalid request (title too short)
        TaskRequestDTO invalidRequest = new TaskRequestDTO();
        invalidRequest.setTitle("AB"); // Less than 3 characters
        invalidRequest.setDescription("Valid description");

        // When & Then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(taskService, never()).create(any(TaskRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /tasks/{id} - Should update task")
    void shouldUpdateTask() throws Exception {
        // Given
        Long taskId = 1L;
        TaskRequestDTO updateRequest = new TaskRequestDTO();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Description");

        TaskResponseDTO updatedResponse = new TaskResponseDTO();
        updatedResponse.setId(taskId);
        updatedResponse.setTitle("Updated Title");
        updatedResponse.setDescription("Updated Description");
        updatedResponse.setTaskStatus(TaskStatus.PENDING);
        updatedResponse.setCreatedAt(LocalDateTime.now());

        when(taskService.update(eq(taskId), any(TaskRequestDTO.class))).thenReturn(updatedResponse);

        // When & Then
        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(taskService, times(1)).update(eq(taskId), any(TaskRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /tasks/{id}/status - Should update task status")
    void shouldUpdateTaskStatus() throws Exception {
        // Given
        Long taskId = 1L;
        TaskStatus newStatus = TaskStatus.COMPLETED;

        TaskResponseDTO updatedResponse = new TaskResponseDTO();
        updatedResponse.setId(taskId);
        updatedResponse.setTitle("Test Task");
        updatedResponse.setDescription("Test Description");
        updatedResponse.setTaskStatus(TaskStatus.COMPLETED);
        updatedResponse.setCreatedAt(LocalDateTime.now());

        when(taskService.updateStatus(taskId, newStatus)).thenReturn(updatedResponse);

        // When & Then
        mockMvc.perform(put("/tasks/{id}/status", taskId)
                        .param("taskStatus", newStatus.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.taskStatus").value("COMPLETED"));

        verify(taskService, times(1)).updateStatus(taskId, newStatus);
    }

    @Test
    @DisplayName("DELETE /tasks/{id} - Should delete task")
    void shouldDeleteTask() throws Exception {
        // Given
        Long taskId = 1L;
        doNothing().when(taskService).delete(taskId);

        // When & Then
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).delete(taskId);
    }

    @Test
    @DisplayName("DELETE /tasks/{id} - Should return 404 when deleting non-existent task")
    void shouldReturn404WhenDeletingNonExistentTask() throws Exception {
        // Given
        Long taskId = 999L;
        doThrow(new ResourceNotFoundException("Tarefa Não Encontrada Para o Id: " + taskId))
                .when(taskService).delete(taskId);

        // When & Then
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).delete(taskId);
    }
}
