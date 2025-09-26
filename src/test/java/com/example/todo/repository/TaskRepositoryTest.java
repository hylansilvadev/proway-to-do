package com.example.todo.repository;

import com.example.todo.model.Task;
import com.example.todo.model.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("TaskRepository Tests")
class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    private Task pendingTask;
    private Task completedTask;

    @BeforeEach
    void setUp() {
        pendingTask = new Task();
        pendingTask.setTitle("Pending Task");
        pendingTask.setDescription("This is a pending task");
        pendingTask.setStatus(TaskStatus.PENDING);
        pendingTask.setCreatedAt(LocalDateTime.now());

        completedTask = new Task();
        completedTask.setTitle("Completed Task");
        completedTask.setDescription("This is a completed task");
        completedTask.setStatus(TaskStatus.COMPLETED);
        completedTask.setCreatedAt(LocalDateTime.now().minusDays(1));
    }

    @Test
    @DisplayName("Should save and find task by ID")
    void shouldSaveAndFindTaskById() {
        // Given
        Task savedTask = entityManager.persistAndFlush(pendingTask);

        // When
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

        // Then
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo("Pending Task");
        assertThat(foundTask.get().getStatus()).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    @DisplayName("Should find all tasks")
    void shouldFindAllTasks() {
        // Given
        entityManager.persistAndFlush(pendingTask);
        entityManager.persistAndFlush(completedTask);

        // When
        List<Task> allTasks = taskRepository.findAll();

        // Then
        assertThat(allTasks).hasSize(2);
        assertThat(allTasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Pending Task", "Completed Task");
    }

    @Test
    @DisplayName("Should find tasks by status")
    void shouldFindTasksByStatus() {
        // Given
        entityManager.persistAndFlush(pendingTask);
        entityManager.persistAndFlush(completedTask);

        // When
        List<Task> pendingTasks = taskRepository.findByTaskStatus(TaskStatus.PENDING);
        List<Task> completedTasks = taskRepository.findByTaskStatus(TaskStatus.COMPLETED);

        // Then
        assertThat(pendingTasks).hasSize(1);
        assertThat(pendingTasks.get(0).getTitle()).isEqualTo("Pending Task");
        assertThat(pendingTasks.get(0).getStatus()).isEqualTo(TaskStatus.PENDING);

        assertThat(completedTasks).hasSize(1);
        assertThat(completedTasks.get(0).getTitle()).isEqualTo("Completed Task");
        assertThat(completedTasks.get(0).getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should return empty list when no tasks with specific status")
    void shouldReturnEmptyListWhenNoTasksWithSpecificStatus() {
        // Given
        entityManager.persistAndFlush(pendingTask);

        // When
        List<Task> completedTasks = taskRepository.findByTaskStatus(TaskStatus.COMPLETED);

        // Then
        assertThat(completedTasks).isEmpty();
    }

    @Test
    @DisplayName("Should update task successfully")
    void shouldUpdateTaskSuccessfully() {
        // Given
        Task savedTask = entityManager.persistAndFlush(pendingTask);
        entityManager.clear();

        // When
        savedTask.setTitle("Updated Title");
        savedTask.setStatus(TaskStatus.COMPLETED);
        Task updatedTask = taskRepository.save(savedTask);

        // Then
        assertThat(updatedTask.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should delete task successfully")
    void shouldDeleteTaskSuccessfully() {
        // Given
        Task savedTask = entityManager.persistAndFlush(pendingTask);
        Long taskId = savedTask.getId();

        // When
        taskRepository.deleteById(taskId);

        // Then
        Optional<Task> deletedTask = taskRepository.findById(taskId);
        assertThat(deletedTask).isEmpty();
    }

    @Test
    @DisplayName("Should check if task exists by ID")
    void shouldCheckIfTaskExistsById() {
        // Given
        Task savedTask = entityManager.persistAndFlush(pendingTask);
        Long existingId = savedTask.getId();
        Long nonExistingId = 999L;

        // When & Then
        assertThat(taskRepository.existsById(existingId)).isTrue();
        assertThat(taskRepository.existsById(nonExistingId)).isFalse();
    }

    @Test
    @DisplayName("Should handle empty database")
    void shouldHandleEmptyDatabase() {
        // Given - empty database

        // When
        List<Task> allTasks = taskRepository.findAll();
        List<Task> pendingTasks = taskRepository.findByTaskStatus(TaskStatus.PENDING);
        List<Task> completedTasks = taskRepository.findByTaskStatus(TaskStatus.COMPLETED);

        // Then
        assertThat(allTasks).isEmpty();
        assertThat(pendingTasks).isEmpty();
        assertThat(completedTasks).isEmpty();
    }

    @Test
    @DisplayName("Should handle multiple tasks with same status")
    void shouldHandleMultipleTasksWithSameStatus() {
        // Given
        Task anotherPendingTask = new Task();
        anotherPendingTask.setTitle("Another Pending Task");
        anotherPendingTask.setDescription("Another pending task description");
        anotherPendingTask.setStatus(TaskStatus.PENDING);
        anotherPendingTask.setCreatedAt(LocalDateTime.now());

        entityManager.persistAndFlush(pendingTask);
        entityManager.persistAndFlush(anotherPendingTask);

        // When
        List<Task> pendingTasks = taskRepository.findByTaskStatus(TaskStatus.PENDING);

        // Then
        assertThat(pendingTasks).hasSize(2);
        assertThat(pendingTasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Pending Task", "Another Pending Task");
    }
}
