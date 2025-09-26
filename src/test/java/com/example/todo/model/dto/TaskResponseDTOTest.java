package com.example.todo.model.dto;

import com.example.todo.model.enums.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskResponseDTO Tests")
class TaskResponseDTOTest {

    @Test
    @DisplayName("Should create TaskResponseDTO with default constructor")
    void shouldCreateTaskResponseDTOWithDefaultConstructor() {
        // Given & When
        TaskResponseDTO dto = new TaskResponseDTO();

        // Then
        assertThat(dto.getId()).isNull();
        assertThat(dto.getTitle()).isNull();
        assertThat(dto.getDescription()).isNull();
        assertThat(dto.getTaskStatus()).isNull();
        assertThat(dto.getCreatedAt()).isNull();
    }

    @Test
    @DisplayName("Should create TaskResponseDTO with all parameters constructor")
    void shouldCreateTaskResponseDTOWithAllParametersConstructor() {
        // Given
        Long id = 1L;
        String title = "Test Task";
        String description = "Test Description";
        TaskStatus status = TaskStatus.PENDING;
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        TaskResponseDTO dto = new TaskResponseDTO(id, title, description, status, createdAt);

        // Then
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getTitle()).isEqualTo(title);
        assertThat(dto.getDescription()).isEqualTo(description);
        assertThat(dto.getTaskStatus()).isEqualTo(status);
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        // Given
        TaskResponseDTO dto = new TaskResponseDTO();
        Long id = 2L;
        String title = "Updated Task";
        String description = "Updated Description";
        TaskStatus status = TaskStatus.COMPLETED;
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        dto.setId(id);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setTaskStatus(status);
        dto.setCreatedAt(createdAt);

        // Then
        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getTitle()).isEqualTo(title);
        assertThat(dto.getDescription()).isEqualTo(description);
        assertThat(dto.getTaskStatus()).isEqualTo(status);
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void shouldHandleNullValuesCorrectly() {
        // Given
        TaskResponseDTO dto = new TaskResponseDTO();

        // When
        dto.setId(null);
        dto.setTitle(null);
        dto.setDescription(null);
        dto.setTaskStatus(null);
        dto.setCreatedAt(null);

        // Then
        assertThat(dto.getId()).isNull();
        assertThat(dto.getTitle()).isNull();
        assertThat(dto.getDescription()).isNull();
        assertThat(dto.getTaskStatus()).isNull();
        assertThat(dto.getCreatedAt()).isNull();
    }
}
