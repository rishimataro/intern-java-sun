package com.example.taskmanager.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;

@Service 
public class TaskService {
    // * Sử dụng List và ArrayList và dùng bộ nhớ RAM, luôn khởi tạo lại dữ liệu mỗi khi chạy chương trình
    // private final List<Task> tasks = new ArrayList<>();
    // private int nextId = 1;

    // public TaskService() {
    //     tasks.add(new Task(1, "Hoc Spring Boot"));
    //     tasks.add(new Task(2, "Hoc Java"));
    //     tasks.add(new Task(3, "Hoc Dependency Injection"));

    //     createTask("Hoc Spring Boot");
    //     createTask("Hoc Java");
    //     createTask("Hoc Dependency Injection");
    // }

    // public List<Task> getAllTasks() {
    //     List<Task> tasks = new ArrayList<>();
    //     tasks.add(new Task(1, "Hoc Spring Boot"));
    //     tasks.add(new Task(2, "Hoc Java"));
    //     tasks.add(new Task(3, "Hoc Dependency Injection"));

    //     return tasks;

    //     return tasks;
    // }

    // public Task getTaskById(int id) {
    //     for (Task task : tasks) {
    //         if(task.getId() == id) {
    //             return task;
    //         }
    //     }
    //     return null;
    // }

    // public Task createTask(String title) {
    //     Task newTask = new Task(nextId, title);
    //     tasks.add(newTask);
    //     nextId++;
    //     return newTask;
    // }

    // public Task updateTask(int id, String title) {
    //     Task task = getTaskById(id);
    //     if (task == null) {
    //         return null;
    //     }
    //     task.setTitle(title);
    //     return task;
    // }

    // public boolean deleteTask(int id) {
    //     Task task = getTaskById(id);
    //     if (task == null) {
    //         return false;
    //     }
    //     tasks.remove(task);
    //     return true;
    // }

    // * Sử dụng H2 Database
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(int id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task createTask(String title) {
        Task task = new Task(title);
        return taskRepository.save(task);
    }

    public Task updateTask(int id, String title) {
        Task task = getTaskById(id);
        if (task == null) return null;

        task.setTitle(title);
        return taskRepository.save(task);
    }

    public boolean deleteTask(int id) {
        Task task = getTaskById(id);
        if (task == null) return false;

        taskRepository.delete(task);
        return true;
    }

    public List<Task> searchTasks(String keyword, String direction) {
        Sort.Direction order = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(order, "id");
        return taskRepository.findByTitleContainingIgnoreCase(keyword, sort);
    }

    public Page<Task> getTaskPage(int page, int size, String direction) {
        Sort.Direction order = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(order, "id"));

        return taskRepository.findAll(pageable);
    }

    public Page<Task> searchTasksPage(String keyword, int page, int size, String direction) {
        Sort.Direction order = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(order, "id");

        Pageable pageable = PageRequest.of(page, size, sort);
        return taskRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }
}