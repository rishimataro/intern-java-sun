package com.example.taskmanager.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;

import jakarta.validation.Valid;

@RequestMapping("/tasks")
@RestController
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // @GetMapping("/tasks/1")
    // public Task getTask() {
    //     return new Task(1, "Hoc Spring Boot");
    // }

    // @GetMapping("/tasks/{id}")
    // public Task getTask(@PathVariable("id") int id) {
    //     return new Task(id, "Hoc Spring Boot" + id);
    // }

    // @GetMapping("/tasks")
    // public List<Task> getAllTasks() {
    //     List<Task> tasks = new ArrayList<>();
    //     tasks.add(new Task(1, "Hoc Spring Boot"));
    //     tasks.add(new Task(2, "Hoc Java"));
    //     tasks.add(new Task(3, "Hoc Python"));

    //     return tasks;
    // }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable ("id") int id) {
        Task task = taskService.getTaskById(id);

        if (task == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Task not found"
            );
        }
        return task;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request.getTitle());
    }
    
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable("id") int id, @Valid @RequestBody CreateTaskRequest request) {
        Task task = taskService.updateTask(id, request.getTitle());
        if (task == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Task not found"
            );
        }
        return task;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("id") int id) {
        boolean deleted = taskService.deleteTask(id);
        if (!deleted) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Task not found"
            );
        }
    }

    // @GetMapping("/tasks/search")
    // public List<Task> searchTask(@RequestParam("keyword") String keyword) {
    //     if (keyword.isBlank()) {
    //         throw new ResponseStatusException(
    //             HttpStatus.BAD_REQUEST, "Keyword must not be blank"
    //         );
    //     }

    //     return taskService.searchTasks(keyword.trim());
    // }

    @GetMapping("/search")
    public List<Task> searchTask(@RequestParam("keyword") String keyword, @RequestParam(value = "sort", defaultValue = "asc") String sort) {
        if (keyword.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Keyword must not be blank"
            );
        }

        if(!sort.equalsIgnoreCase("asc") && !sort.equalsIgnoreCase("desc")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Sort must be asc or desc"
            );
        }

        return taskService.searchTasks(keyword.trim(), sort);
    }

    @GetMapping("/page")
    public PagedModel<Task> getTasksPage(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "2") int size,
        @RequestParam(value = "sort", defaultValue = "asc") String sort
    ) {
        if (page < 0 || size < 1 || size > 20) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page or size is invalid"
            );
        }

        if(!sort.equalsIgnoreCase("asc") && !sort.equalsIgnoreCase("desc")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Sort must be asc or desc"
            );
        }

        Page<Task> result = taskService.getTaskPage(page, size, sort);

        return new PagedModel<>(result);
    }
    
    @GetMapping("/search/page")
    public PagedModel<Task> searchTasksPage(
        @RequestParam("keyword") String keyword, 
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "2") int size,
        @RequestParam(value = "sort", defaultValue = "asc") String sort
    )
    {
        if (keyword.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Keyword must not be blank"
            );
        }

        if (page < 0 || size < 1 || size > 20) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Page or size is invalid"
            );
        }

        if(!sort.equalsIgnoreCase("asc") && !sort.equalsIgnoreCase("desc")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Sort must be asc or desc"
            );
        }

        Page<Task> result = taskService.searchTasksPage(keyword.trim(), page, size, sort);

        return new PagedModel<>(result);
    }
}
