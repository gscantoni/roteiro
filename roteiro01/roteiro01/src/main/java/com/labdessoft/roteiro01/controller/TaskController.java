package com.labdessoft.roteiro01.controller;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskType;
import com.labdessoft.roteiro01.entity.Priority;
import com.labdessoft.roteiro01.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping
    @Operation(summary = "Adiciona uma nova tarefa")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        if (task.getTaskType() == TaskType.DATE && (task.getDueDate() == null || task.getDueDate().isBefore(LocalDate.now()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (task.getTaskType() == TaskType.DEADLINE && task.getDeadlineInDays() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Task savedTask = taskRepository.save(task);
            return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma tarefa pelo seu ID")
    public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @Valid @RequestBody Task taskDetails) {
        Task existingTask = taskRepository.findById(id).orElse(null);

        if (existingTask == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (taskDetails.getTaskType() == TaskType.DATE && (taskDetails.getDueDate() == null || taskDetails.getDueDate().isBefore(LocalDate.now()))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (taskDetails.getTaskType() == TaskType.DEADLINE && taskDetails.getDeadlineInDays() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setCompleted(taskDetails.getCompleted());
        existingTask.setDueDate(taskDetails.getDueDate());
        existingTask.setDeadlineInDays(taskDetails.getDeadlineInDays());
        existingTask.setPriority(taskDetails.getPriority());
        existingTask.setTaskType(taskDetails.getTaskType());
        existingTask.setStatus(existingTask.determineStatus());

        try {
            final Task updatedTask = taskRepository.save(existingTask);
            return ResponseEntity.ok(updatedTask);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma tarefa pelo seu ID")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") long id) {
        try {
            taskRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recupera uma tarefa pelo seu ID")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long id) {
        Task task = taskRepository.findById(id).orElse(null);

        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        task.setStatus(task.determineStatus());
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/priority/{priority}")
    @Operation(summary = "Recupera tarefas pela prioridade")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable("priority") Priority priority) {
        try {
            List<Task> tasks = taskRepository.findByPriority(priority);
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Recupera tarefas pelo status")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable("status") String status) {
        try {
            List<Task> tasks = taskRepository.findByStatus(status);
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
