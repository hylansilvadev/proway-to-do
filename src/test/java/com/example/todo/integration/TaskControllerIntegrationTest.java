package com.example.todo.integration;

import com.example.todo.model.dto.TaskRequestDTO;
import com.example.todo.model.dto.TaskResponseDTO;
import com.example.todo.model.enums.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("TaskController Integration Tests")
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Full CRUD Integration Test")
    @Transactional
    void shouldPerformFullCrudOperations() throws Exception {
        // 1. CREATE - Create a new task
        TaskRequestDTO createRequest = new TaskRequestDTO();
        createRequest.setTitle("Integration Test Task");
        createRequest.setDescription("This is an integration test task");

        String createResponse = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andExpect(jsonPath("$.description").value("This is an integration test task"))
                .andExpect(jsonPath("$.taskStatus").value("PENDING"))
                .andReturn().getResponse().getContentAsString();

        TaskResponseDTO createdTask = objectMapper.readValue(createResponse, TaskResponseDTO.class);
        Long taskId = createdTask.getId();

        // 2. READ - Get the created task by ID
        mockMvc.perform(get("/tasks/{id}", taskId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andExpect(jsonPath("$.taskStatus").value("PENDING"));

        // 3. READ - Get all tasks (should include our created task)
        mockMvc.perform(get("/tasks"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // 4. READ - Get tasks by status
        mockMvc.perform(get("/tasks/status/{status}", TaskStatus.PENDING))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        // 5. UPDATE - Update the task
        TaskRequestDTO updateRequest = new TaskRequestDTO();
        updateRequest.setTitle("Updated Integration Test Task");
        updateRequest.setDescription("This task has been updated");

        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Updated Integration Test Task"))
                .andExpect(jsonPath("$.description").value("This task has been updated"));

        // 6. UPDATE STATUS - Update task status
        mockMvc.perform(put("/tasks/{id}/status", taskId)
                        .param("taskStatus", TaskStatus.COMPLETED.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.taskStatus").value("COMPLETED"));

        // 7. DELETE - Delete the task
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andDo(print())
                .andExpect(status().isNoContent());

        // 8. Verify deletion - Try to get the deleted task (should return 404)
        mockMvc.perform(get("/tasks/{id}", taskId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should handle validation errors")
    @Transactional
    void shouldHandleValidationErrors() throws Exception {
        // Test with invalid title (too short)
        TaskRequestDTO invalidRequest = new TaskRequestDTO();
        invalidRequest.setTitle("AB"); // Less than 3 characters
        invalidRequest.setDescription("Valid description");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // Test with missing title
        TaskRequestDTO missingTitleRequest = new TaskRequestDTO();
        missingTitleRequest.setDescription("Valid description");

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missingTitleRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // Test with description too long
        TaskRequestDTO longDescriptionRequest = new TaskRequestDTO();
        longDescriptionRequest.setTitle("Valid Title");
        longDescriptionRequest.setDescription("A".repeat(301)); // More than 300 characters

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(longDescriptionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle not found errors")
    @Transactional
    void shouldHandleNotFoundErrors() throws Exception {
        Long nonExistentId = 999999L;

        // Test GET with non-existent ID
        mockMvc.perform(get("/tasks/{id}", nonExistentId))
                .andDo(print())
                .andExpect(status().isNotFound());

        // Test PUT with non-existent ID
        TaskRequestDTO updateRequest = new TaskRequestDTO();
        updateRequest.setTitle("Valid Title");
        updateRequest.setDescription("Valid Description");

        mockMvc.perform(put("/tasks/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());

        // Test PUT status with non-existent ID
        mockMvc.perform(put("/tasks/{id}/status", nonExistentId)
                        .param("taskStatus", TaskStatus.COMPLETED.toString()))
                .andDo(print())
                .andExpect(status().isNotFound());

        // Test DELETE with non-existent ID
        mockMvc.perform(delete("/tasks/{id}", nonExistentId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
