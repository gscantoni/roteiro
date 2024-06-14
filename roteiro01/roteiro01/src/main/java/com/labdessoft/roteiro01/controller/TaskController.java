package com.labdessoft.roteiro01.controller;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.enums.Priority;
import com.labdessoft.roteiro01.enums.TaskType;
import com.labdessoft.roteiro01.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskService service;

    @GetMapping("/tasks")
    @Operation(summary = "Obtém todas as tarefas")
    public ResponseEntity<List<Task>> getAllTasks() {
        try {
            List<Task> tasks = service.getAllTasks();
            if (tasks.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/task/{id}")
    @Operation(summary = "Obtém uma tarefa por ID")
    public ResponseEntity<Task> findTaskById(@PathVariable long id) {
        try {
            Task task = service.getTaskById(id);
            if (task == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            task.setStatus(task.determineStatus());
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/taskCreate")
    @Operation(summary = "Cria uma nova tarefa")
    public ResponseEntity<Task> addNewTask(@Valid @RequestBody Task task) {
        if ((task.getTaskType() == TaskType.DATE && (task.getDueDate() == null || task.getDueDate().isBefore(LocalDate.now()))) || 
            (task.getTaskType() == TaskType.DEADLINE && task.getDeadlineInDays() == null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            Task newTask = service.createTask(task);
            return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/complete/{id}")
    @Operation(summary = "Marca uma tarefa como concluída")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable long id) {
        try {
            Task completedTask = service.completeTask(id);
            if (completedTask == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(completedTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/taskEdit/{id}")
    @Operation(summary = "Atualiza uma tarefa existente")
    public ResponseEntity<Task> modifyTask(@PathVariable long id, @Valid @RequestBody Task taskDetails) {
        if ((taskDetails.getTaskType() == TaskType.DATE && (taskDetails.getDueDate() == null || taskDetails.getDueDate().isBefore(LocalDate.now()))) || 
            (taskDetails.getTaskType() == TaskType.DEADLINE && taskDetails.getDeadlineInDays() == null)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            Task updatedTask = service.updateTask(id, taskDetails);
            if (updatedTask == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/taskRemove/{id}")
    @Operation(summary = "Remove uma tarefa pelo ID")
    public ResponseEntity<HttpStatus> removeTask(@PathVariable long id) {
        try {
            boolean deleted = service.deleteTask(id);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/taskPriority/{priority}")
    @Operation(summary = "Obtém tarefas pela prioridade")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable Priority priority) {
        try {
            List<Task> tasks = service.getTasksByPriority(priority);
            if (tasks.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/taskStatus/{status}")
    @Operation(summary = "Obtém tarefas pelo status")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        try {
            List<Task> tasks = service.getTasksByStatus(status);
            if (tasks.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
