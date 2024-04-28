package com.labdessoft.roteiro01.controller;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.repository.TaskRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping
    @Operation(summary = "Adiciona uma nova tarefa")
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        if (task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now())) {
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

        if (taskDetails.getDueDate() != null && taskDetails.getDueDate().isBefore(LocalDate.now())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setCompleted(taskDetails.getCompleted());
        existingTask.setDueDate(taskDetails.getDueDate());
        existingTask.setPriority(taskDetails.getPriority());

        final Task updatedTask = taskRepository.save(existingTask);
        return ResponseEntity.ok(updatedTask);
    }


    @GetMapping("/filterByDate")
    @Operation(summary = "Filtra pela data")
    public ResponseEntity<List<Task>> findByDueDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Task> tasks = taskRepository.findAll().stream()
                          .filter(t -> t.getDueDate() != null && t.getDueDate().equals(date))
                          .collect(Collectors.toList());
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
}