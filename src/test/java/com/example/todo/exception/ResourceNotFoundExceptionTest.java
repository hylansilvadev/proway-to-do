package com.example.todo.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ResourceNotFoundException Tests")
class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        // Given
        String message = "Resource not found";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should be throwable")
    void shouldBeThrowable() {
        // Given
        String message = "Test exception message";

        // When & Then
        assertThatThrownBy(() -> {
            throw new ResourceNotFoundException(message);
        })
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(message);
    }

    @Test
    @DisplayName("Should handle null message")
    void shouldHandleNullMessage() {
        // Given & When
        ResourceNotFoundException exception = new ResourceNotFoundException(null);

        // Then
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    @DisplayName("Should handle empty message")
    void shouldHandleEmptyMessage() {
        // Given
        String message = "";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo("");
    }
}
