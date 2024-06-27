// File: src/test/java/com/labdessoft/roteiro01/service/TaskServiceTest.java

package com.labdessoft.roteiro01.service;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.enums.Priority;
import com.labdessoft.roteiro01.enums.TaskType;
import com.labdessoft.roteiro01.mock.TaskMock;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = TaskMock.createTasks();
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        Task task = TaskMock.createTask(1);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertEquals(1L, result.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.getTaskById(1L);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateTask() {
        Task task = TaskMock.createTask(3);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals("Valid description", result.getDescription());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testCompleteTask() {
        Task task = TaskMock.createTask(1);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.completeTask(1L);

        assertTrue(result.getCompleted());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTask() {
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setDescription("Updated description");
        updatedTask.setTaskType(TaskType.DATE);
        updatedTask.setDueDate(LocalDate.now().plusDays(5));
        updatedTask.setCompleted(false);

        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("Updated description", result.getDescription());
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).save(updatedTask);
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        boolean result = taskService.deleteTask(1L);

        assertTrue(result);
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetTasksByPriority() {
        List<Task> tasks = TaskMock.createTasks();
        when(taskRepository.findByPriority(Priority.HIGH)).thenReturn(tasks);

        List<Task> result = taskService.getTasksByPriority(Priority.HIGH);

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findByPriority(Priority.HIGH);
    }

    @Test
    void testGetTasksByStatus() {
        List<Task> tasks = TaskMock.createTasks();
        when(taskRepository.findByStatus("Scheduled")).thenReturn(tasks);

        List<Task> result = taskService.getTasksByStatus("Scheduled");

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findByStatus("Scheduled");
    }
}
