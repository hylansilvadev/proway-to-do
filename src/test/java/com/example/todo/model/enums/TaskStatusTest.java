package com.example.todo.model.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskStatus Enum Tests")
class TaskStatusTest {

    @Test
    @DisplayName("Should have PENDING status")
    void shouldHavePendingStatus() {
        // Given & When
        TaskStatus status = TaskStatus.PENDING;

        // Then
        assertThat(status).isNotNull();
        assertThat(status.name()).isEqualTo("PENDING");
        assertThat(status.toString()).isEqualTo("PENDING");
    }

    @Test
    @DisplayName("Should have COMPLETED status")
    void shouldHaveCompletedStatus() {
        // Given & When
        TaskStatus status = TaskStatus.COMPLETED;

        // Then
        assertThat(status).isNotNull();
        assertThat(status.name()).isEqualTo("COMPLETED");
        assertThat(status.toString()).isEqualTo("COMPLETED");
    }

    @Test
    @DisplayName("Should have exactly two values")
    void shouldHaveExactlyTwoValues() {
        // Given & When
        TaskStatus[] values = TaskStatus.values();

        // Then
        assertThat(values).hasSize(2);
        assertThat(values).containsExactlyInAnyOrder(TaskStatus.PENDING, TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should be able to valueOf from string")
    void shouldBeAbleToValueOfFromString() {
        // Given & When
        TaskStatus pendingFromString = TaskStatus.valueOf("PENDING");
        TaskStatus completedFromString = TaskStatus.valueOf("COMPLETED");

        // Then
        assertThat(pendingFromString).isEqualTo(TaskStatus.PENDING);
        assertThat(completedFromString).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should maintain enum order")
    void shouldMaintainEnumOrder() {
        // Given & When
        TaskStatus[] values = TaskStatus.values();

        // Then
        assertThat(values[0]).isEqualTo(TaskStatus.PENDING);
        assertThat(values[1]).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should support ordinal values")
    void shouldSupportOrdinalValues() {
        // Given & When & Then
        assertThat(TaskStatus.PENDING.ordinal()).isEqualTo(0);
        assertThat(TaskStatus.COMPLETED.ordinal()).isEqualTo(1);
    }
}
