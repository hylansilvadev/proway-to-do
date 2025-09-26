package com.example.todo.model;

import com.example.todo.model.enums.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Task Entity Tests")
class TaskTest {

    @Test
    @DisplayName("Should create Task with default constructor")
    void shouldCreateTaskWithDefaultConstructor() {
        // Given & When
        Task task = new Task();

        // Then
        assertThat(task.getId()).isEqualTo(0L);
        assertThat(task.getTitle()).isNull();
        assertThat(task.getDescription()).isNull();
        assertThat(task.getStatus()).isNull();
        assertThat(task.getCreatedAt()).isNotNull(); // LocalDateTime.now() is set by default
    }

    @Test
    @DisplayName("Should create Task with all parameters constructor")
    void shouldCreateTaskWithAllParametersConstructor() {
        // Given
        long id = 1L;
        String title = "Test Task";
        String description = "Test Description";
        TaskStatus status = TaskStatus.PENDING;
        LocalDateTime createdAt = LocalDateTime.now();

        // When
        Task task = new Task(id, title, description, status, createdAt);

        // Then
        assertThat(task.getId()).isEqualTo(id);
        assertThat(task.getTitle()).isEqualTo(title);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getStatus()).isEqualTo(status);
        assertThat(task.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        // Given
        Task task = new Task();
        long id = 2L;
        String title = "Updated Task";
        String description = "Updated Description";
        TaskStatus status = TaskStatus.COMPLETED;
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);

        // When
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setCreatedAt(createdAt);

        // Then
        assertThat(task.getId()).isEqualTo(id);
        assertThat(task.getTitle()).isEqualTo(title);
        assertThat(task.getDescription()).isEqualTo(description);
        assertThat(task.getStatus()).isEqualTo(status);
        assertThat(task.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void shouldHandleNullValuesCorrectly() {
        // Given
        Task task = new Task();

        // When
        task.setTitle(null);
        task.setDescription(null);
        task.setStatus(null);
        task.setCreatedAt(null);

        // Then
        assertThat(task.getTitle()).isNull();
        assertThat(task.getDescription()).isNull();
        assertThat(task.getStatus()).isNull();
        assertThat(task.getCreatedAt()).isNull();
    }

    @Test
    @DisplayName("Should have default createdAt when using default constructor")
    void shouldHaveDefaultCreatedAtWhenUsingDefaultConstructor() {
        // Given
        LocalDateTime beforeCreation = LocalDateTime.now().minusSeconds(1);

        // When
        Task task = new Task();
        LocalDateTime afterCreation = LocalDateTime.now().plusSeconds(1);

        // Then
        assertThat(task.getCreatedAt()).isBetween(beforeCreation, afterCreation);
    }

    @Test
    @DisplayName("Should allow updating all task statuses")
    void shouldAllowUpdatingAllTaskStatuses() {
        // Given
        Task task = new Task();

        // When & Then
        task.setStatus(TaskStatus.PENDING);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);

        task.setStatus(TaskStatus.COMPLETED);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should handle empty strings correctly")
    void shouldHandleEmptyStringsCorrectly() {
        // Given
        Task task = new Task();

        // When
        task.setTitle("");
        task.setDescription("");

        // Then
        assertThat(task.getTitle()).isEqualTo("");
        assertThat(task.getDescription()).isEqualTo("");
    }
}
