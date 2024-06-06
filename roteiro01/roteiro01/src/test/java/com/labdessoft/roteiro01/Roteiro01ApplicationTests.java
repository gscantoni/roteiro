package com.labdessoft.roteiro01;

import com.labdessoft.roteiro01.entity.Priority;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskType;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class Roteiro01ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateTask() throws Exception {
        String taskJson = "{ \"title\": \"New Task\", \"description\": \"This is a new task\", \"completed\": false, \"dueDate\": \"2024-12-31\", \"priority\": \"HIGH\", \"taskType\": \"DATE\" }";

        mockMvc.perform(post("/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("New Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("This is a new task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("HIGH"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskType").value("DATE"));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task task = new Task("Task to Update", "This task will be updated", false, LocalDate.now().plusDays(5), 5, Priority.MEDIUM, TaskType.DEADLINE);
        task = taskRepository.save(task);

        String taskUpdateJson = "{ \"title\": \"Updated Task\", \"description\": \"This is an updated task\", \"completed\": true, \"dueDate\": \"2024-12-31\", \"priority\": \"LOW\", \"taskType\": \"DATE\" }";

        mockMvc.perform(put("/task/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskUpdateJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("This is an updated task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("LOW"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskType").value("DATE"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Task task = new Task("Task to Delete", "This task will be deleted", false, LocalDate.now().plusDays(5), 5, Priority.LOW, TaskType.FREE);
        task = taskRepository.save(task);

        mockMvc.perform(delete("/task/" + task.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task task = new Task("Task to Retrieve", "This task will be retrieved", false, LocalDate.now().plusDays(5), 5, Priority.HIGH, TaskType.DEADLINE);
        task = taskRepository.save(task);

        mockMvc.perform(get("/task/" + task.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Task to Retrieve"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("This task will be retrieved"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("HIGH"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskType").value("DEADLINE"));
    }
}
