package com.labdessoft.roteiro01.controller;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    @Operation(summary = "Lista todas as tarefas")
    public ResponseEntity<List<Task>> listAll() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Adiciona uma nova tarefa")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        try {
            Task _task = taskRepository.save(new Task(task.getTitle(), task.getDescription(), task.getCompleted()));
            return new ResponseEntity<>(_task, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui uma tarefa pelo seu ID")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("id") long id) {
        try {
            taskRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma tarefa pelo seu ID")
    public ResponseEntity<Task> updateTask(@PathVariable("id") long id, @RequestBody Task taskDetails) {
        Task task = taskRepository.findById(id)
                .orElse(null);

        if (task == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.getCompleted());

        final Task updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }
}




