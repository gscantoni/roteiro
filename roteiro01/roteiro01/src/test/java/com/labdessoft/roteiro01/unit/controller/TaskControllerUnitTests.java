package com.labdessoft.roteiro01.unit.controller;

import com.labdessoft.roteiro01.controller.TaskController;
import com.labdessoft.roteiro01.entity.Priority;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskType;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerUnitTests {

    private MockMvc mockMvc;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void shouldCreateTask() throws Exception {
        Task task = new Task("New Task", "This is a new task", Priority.HIGH, TaskType.DATE, LocalDate.of(2024, 12, 31));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        String taskJson = "{ \"title\": \"New Task\", \"description\": \"This is a new task\", \"completed\": false, \"dueDate\": \"2024-12-31\", \"priority\": \"HIGH\", \"taskType\": \"DATE\" }";

        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("This is a new task"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.taskType").value("DATE"));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task existingTask = new Task("Task to Update", "This task will be updated", false, LocalDate.now().plusDays(5), 5, Priority.MEDIUM, TaskType.DEADLINE);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        String taskUpdateJson = "{ \"title\": \"Updated Task\", \"description\": \"This is an updated task\", \"completed\": true, \"dueDate\": \"2024-12-31\", \"priority\": \"LOW\", \"taskType\": \"DATE\" }";

        mockMvc.perform(put("/tasks/" + existingTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskUpdateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("This is an updated task"))
                .andExpect(jsonPath("$.completed").value(true))
                .andExpect(jsonPath("$.priority").value("LOW"))
                .andExpect(jsonPath("$.taskType").value("DATE"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Task task = new Task("Task to Delete", "This task will be deleted", false, LocalDate.now().plusDays(5), 5, Priority.LOW, TaskType.FREE);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task task = new Task("Task to Retrieve", "This task will be retrieved", false, LocalDate.now().plusDays(5), 5, Priority.HIGH, TaskType.DEADLINE);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task to Retrieve"))
                .andExpect(jsonPath("$.description").value("This task will be retrieved"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.taskType").value("DEADLINE"));
    }
}
