package com.example.taskmanager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskmanager.model.Task;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByTitleContainingIgnoreCase(String keyword, Sort sort);
    Page<Task> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
