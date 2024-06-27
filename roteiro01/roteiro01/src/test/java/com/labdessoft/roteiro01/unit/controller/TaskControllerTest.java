package com.labdessoft.roteiro01.unit.controller;

import com.labdessoft.roteiro01.controller.TaskController;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.enums.Priority;
import com.labdessoft.roteiro01.enums.TaskType;
import com.labdessoft.roteiro01.mock.TaskMock;
import com.labdessoft.roteiro01.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private List<Task> tasks;

    @BeforeEach
    void setUp() {
        tasks = TaskMock.createTasks();
    }

    @Test
    @DisplayName("Should list all tasks")
    void shouldListAllTasks() {
        when(taskService.getAllTasks()).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.getAllTasks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tasks, response.getBody());
    }

    @Test
    @DisplayName("Should return no content when no tasks are found")
    void shouldReturnNoContentWhenNoTasksAreFound() {
        when(taskService.getAllTasks()).thenReturn(Arrays.asList());

        ResponseEntity<List<Task>> response = taskController.getAllTasks();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return a task by ID")
    void shouldReturnTaskById() {
        Task task = TaskMock.createTask(1);
        when(taskService.getTaskById(1L)).thenReturn(task);

        ResponseEntity<Task> response = taskController.findTaskById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(task, response.getBody());
    }

    @Test
    @DisplayName("Should return not found when task is not found")
    void shouldReturnNotFoundWhenTaskIsNotFound() {
        when(taskService.getTaskById(1L)).thenReturn(null);

        ResponseEntity<Task> response = taskController.findTaskById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should create a new task")
    void shouldCreateNewTask() {
        Task task = new Task();
        task.setDescription("Valid description");
        task.setTaskType(TaskType.DATE);
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setPriority(Priority.HIGH);

        when(taskService.createTask(any(Task.class))).thenReturn(task);

        ResponseEntity<Task> response = taskController.addNewTask(task);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(task, response.getBody());
    }

    @Test
    @DisplayName("Should complete a task")
    void shouldCompleteTask() {
        Task task = TaskMock.createTask(1);
        task.setCompleted(true);
        when(taskService.completeTask(1L)).thenReturn(task);

        ResponseEntity<Task> response = taskController.markTaskAsCompleted(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(task, response.getBody());
    }

    @Test
    @DisplayName("Should update a task")
    void shouldUpdateTask() {
        Task task = TaskMock.createTask(1);
        task.setDescription("Updated Task");
        when(taskService.updateTask(anyLong(), any(Task.class))).thenReturn(task);

        ResponseEntity<Task> response = taskController.modifyTask(1L, task);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(task, response.getBody());
    }

    @Test
    @DisplayName("Should delete a task")
    void shouldDeleteTask() {
        when(taskService.deleteTask(1L)).thenReturn(true);

        ResponseEntity<HttpStatus> response = taskController.removeTask(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return not found when deleting a non-existing task")
    void shouldReturnNotFoundWhenDeletingNonExistingTask() {
        when(taskService.deleteTask(1L)).thenReturn(false);

        ResponseEntity<HttpStatus> response = taskController.removeTask(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should get tasks by priority")
    void shouldGetTasksByPriority() {
        when(taskService.getTasksByPriority(Priority.HIGH)).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.getTasksByPriority(Priority.HIGH);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tasks, response.getBody());
    }

    @Test
    @DisplayName("Should get tasks by status")
    void shouldGetTasksByStatus() {
        when(taskService.getTasksByStatus("Scheduled")).thenReturn(tasks);

        ResponseEntity<List<Task>> response = taskController.getTasksByStatus("Scheduled");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(tasks, response.getBody());
    }
}
