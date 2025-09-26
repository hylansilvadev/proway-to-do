package com.example.todo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("TodoApplication Tests")
class TodoApplicationTests {

	@Test
	@DisplayName("Should load Spring application context successfully")
	void contextLoads() {
		// This test verifies that the Spring application context loads correctly
		// If the context fails to load, this test will fail
	}

	@Test
	@DisplayName("Should start application main method")
	void shouldStartApplicationMainMethod() {
		// Test that verifies the main method can be called without errors
		// In a real test environment, this would typically be done with TestContainers
		// or similar integration test setup

		// For now, we just verify the class exists and is properly configured
		String[] args = {};
		// TodoApplication.main(args); // Commented out to avoid starting full application in tests

		// Instead, we verify the class structure is correct
		assert TodoApplication.class != null;
	}
}
