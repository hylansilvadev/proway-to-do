package com.example.todo.model.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TaskRequestDTO Validation Tests")
class TaskRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should create valid TaskRequestDTO")
    void shouldCreateValidTaskRequestDTO() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Valid Task Title");
        dto.setDescription("Valid description");

        // When
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
        assertThat(dto.getTitle()).isEqualTo("Valid Task Title");
        assertThat(dto.getDescription()).isEqualTo("Valid description");
    }

    @Test
    @DisplayName("Should create TaskRequestDTO with constructor")
    void shouldCreateTaskRequestDTOWithConstructor() {
        // Given & When
        TaskRequestDTO dto = new TaskRequestDTO("Test Title", "Test Description");

        // Then
        assertThat(dto.getTitle()).isEqualTo("Test Title");
        assertThat(dto.getDescription()).isEqualTo("Test Description");
    }

    @Test
    @DisplayName("Should fail validation when title is null")
    void shouldFailValidationWhenTitleIsNull() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setDescription("Valid description");

        // When
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Título é obrigatório");
    }

    @Test
    @DisplayName("Should fail validation when title is blank")
    void shouldFailValidationWhenTitleIsBlank() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("   ");
        dto.setDescription("Valid description");

        // When
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Título é obrigatório");
    }

    @Test
    @DisplayName("Should fail validation when title is too short")
    void shouldFailValidationWhenTitleIsTooShort() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("AB"); // 2 characters, minimum is 3
        dto.setDescription("Valid description");

        // When
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Título deve estar entre 3 e 75 caracteres");
    }

    @Test
    @DisplayName("Should fail validation when title is too long")
    void shouldFailValidationWhenTitleIsTooLong() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("A".repeat(76)); // 76 characters, maximum is 75
        dto.setDescription("Valid description");

        // When
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Título deve estar entre 3 e 75 caracteres");
    }

    @Test
    @DisplayName("Should fail validation when description is too long")
    void shouldFailValidationWhenDescriptionIsTooLong() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Valid Title");
        dto.setDescription("A".repeat(301)); // 301 characters, maximum is 300

        // When
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Descrição não pode ser maior do que 300 caracteres");
    }

    @Test
    @DisplayName("Should allow null description")
    void shouldAllowNullDescription() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Valid Title");
        dto.setDescription(null);

        // When
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should allow empty description")
    void shouldAllowEmptyDescription() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Valid Title");
        dto.setDescription("");

        // When
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should allow maximum length title")
    void shouldAllowMaximumLengthTitle() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("A".repeat(75)); // Exactly 75 characters
        dto.setDescription("Valid description");

        // When
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should allow maximum length description")
    void shouldAllowMaximumLengthDescription() {
        // Given
        TaskRequestDTO dto = new TaskRequestDTO();
        dto.setTitle("Valid Title");
        dto.setDescription("A".repeat(300)); // Exactly 300 characters

        // When
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(dto);

        // Then
        assertThat(violations).isEmpty();
    }
}
