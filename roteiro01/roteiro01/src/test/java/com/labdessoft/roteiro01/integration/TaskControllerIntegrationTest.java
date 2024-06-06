package com.labdessoft.roteiro01.integration;

import com.labdessoft.roteiro01.Roteiro01Application;
import com.labdessoft.roteiro01.entity.Priority;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Roteiro01Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("Description for new task");
        task.setPriority(Priority.HIGH);
        task.setTaskType(TaskType.DATE);
        task.setDueDate(LocalDate.now().plusDays(5));

        given()
            .contentType("application/json")
            .body(task)
        .when()
            .post("/tasks") 
        .then()
            .statusCode(201)
            .body("title", equalTo("New Task"))
            .body("description", equalTo("Description for new task"));
    }

    @Test
    public void testGetTaskById() {
        Task task = restTemplate.postForObject("/tasks", new Task("Task 1", "Description", Priority.MEDIUM, TaskType.DATE, LocalDate.now().plusDays(5)), Task.class);

        given()
        .when()
            .get("/tasks/{id}", task.getId())  
        .then()
            .statusCode(200)
            .body("title", equalTo("Task 1"))
            .body("description", equalTo("Description"));
    }

    @Test
    public void testUpdateTask() {
        Task task = restTemplate.postForObject("/tasks", new Task("Task to Update", "Initial Description", Priority.LOW, TaskType.DATE, LocalDate.now().plusDays(5)), Task.class);
        task.setTitle("Updated Task");
        task.setDescription("Updated Description");

        given()
            .contentType("application/json")
            .body(task)
        .when()
            .put("/tasks/{id}", task.getId())  
        .then()
            .statusCode(200)
            .body("title", equalTo("Updated Task"))
            .body("description", equalTo("Updated Description"));
    }

    @Test
    public void testDeleteTask() {
        Task task = restTemplate.postForObject("/tasks", new Task("Task to Delete", "Description", Priority.MEDIUM, TaskType.DATE, LocalDate.now().plusDays(5)), Task.class);

        given()
        .when()
            .delete("/tasks/{id}", task.getId()) 
        .then()
            .statusCode(204);
    }

    @Test
    public void testGetTasksByPriority() {
        restTemplate.postForObject("/tasks", new Task("High Priority Task", "Description", Priority.HIGH, TaskType.DATE, LocalDate.now().plusDays(5)), Task.class);
        restTemplate.postForObject("/tasks", new Task("Medium Priority Task", "Description", Priority.MEDIUM, TaskType.DATE, LocalDate.now().plusDays(5)), Task.class);

        given()
        .when()
            .get("/tasks/priority/HIGH") 
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].priority", equalTo("HIGH"));
    }

    @Test
    public void testGetTasksByStatus() {
        Task completedTask = new Task("Completed Task", "Description", Priority.LOW, TaskType.DATE, LocalDate.now().plusDays(5));
        completedTask.setCompleted(true);
        restTemplate.postForObject("/tasks", completedTask, Task.class);

        given()
        .when()
            .get("/tasks/status/COMPLETED")  
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].status", equalTo("COMPLETED"));
    }
}
