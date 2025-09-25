package com.example.todo.repository;

import com.example.todo.model.Task;
import com.example.todo.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTaskStatus(TaskStatus taskStatus);
    List<Task> findByTitleContainingIgnoreCase(String title);
}
