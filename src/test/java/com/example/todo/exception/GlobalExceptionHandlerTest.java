package com.example.todo.exception;

import com.example.todo.controller.TaskController;
import com.example.todo.model.dto.TaskRequestDTO;
import com.example.todo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should handle ResourceNotFoundException and return 404")
    void shouldHandleResourceNotFoundExceptionAndReturn404() throws Exception {
        // Given
        Long taskId = 999L;
        when(taskService.findById(taskId))
                .thenThrow(new ResourceNotFoundException("Tarefa Não Encontrada Para o Id: " + taskId));

        // When & Then
        mockMvc.perform(get("/tasks/{id}", taskId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Tarefa Não Encontrada Para o Id: " + taskId));
    }

    @Test
    @DisplayName("Should handle validation errors and return 400")
    void shouldHandleValidationErrorsAndReturn400() throws Exception {
        // Given - Invalid request with title too short
        TaskRequestDTO invalidRequest = new TaskRequestDTO();
        invalidRequest.setTitle("AB"); // Less than 3 characters
        invalidRequest.setDescription("Valid description");

        // When & Then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").exists()); // Validation errors return field name as key
    }

    @Test
    @DisplayName("Should handle missing request body and return 400")
    void shouldHandleMissingRequestBodyAndReturn400() throws Exception {
        // When & Then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle malformed JSON and return 400")
    void shouldHandleMalformedJSONAndReturn400() throws Exception {
        // Given - Malformed JSON
        String malformedJson = "{ \"title\": \"Test\", \"description\": }";

        // When & Then
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
