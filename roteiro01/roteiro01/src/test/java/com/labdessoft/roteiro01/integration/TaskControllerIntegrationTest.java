// src/test/java/com/labdessoft/roteiro01/integration/TaskControllerIntegrationTest.java

package com.labdessoft.roteiro01.integration;

import com.labdessoft.roteiro01.Roteiro01Application;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.enums.Priority;
import com.labdessoft.roteiro01.enums.TaskType;
import com.labdessoft.roteiro01.repository.TaskRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {Roteiro01Application.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    private Long taskId;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080/api";
        RestAssured.port = 8080;

        Task task = new Task();
        task.setDescription("Tarefa para testes");
        task.setCompleted(false);
        task.setDueDate(LocalDate.of(2024, 5, 10));
        task.setDeadlineInDays(5);
        task.setTaskType(TaskType.DEADLINE);
        task.setPriority(Priority.MEDIUM);

        Task savedTask = taskRepository.save(task);
        taskId = savedTask.getId();
    }

    @Test
    public void givenUrl_whenSuccessOnGetsResponseAndJsonHasRequiredKV_thenCorrect() {
        get("/tasks").then().statusCode(200);
    }

    @Test
    public void givenUrl_whenSuccessOnPostCreatesTask_thenCorrect() {
        Task task = new Task();
        task.setDescription("Nova Tarefa para testes");
        task.setCompleted(false);
        task.setDueDate(LocalDate.of(2024, 6, 10));
        task.setDeadlineInDays(10);
        task.setTaskType(TaskType.DEADLINE);
        task.setPriority(Priority.HIGH);

        given()
            .contentType(ContentType.JSON)
            .body(task)
        .when()
            .post("/taskCreate")
        .then()
            .statusCode(201)
            .body("description", equalTo("Nova Tarefa para testes"));
    }

    @Test
    public void givenUrl_whenSuccessOnGetsResponseAndJsonHasOneTask_thenCorrect() {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            when()
                .get("/task/" + taskId)
            .then()
                .statusCode(200)
                .body("description", equalTo("Tarefa para testes"));
        }
    }

    @Test
    public void givenUrl_whenSuccessOnPutCompletesTask_thenCorrect() {
        when()
            .put("/complete/" + taskId)
        .then()
            .statusCode(200)
            .body("completed", equalTo(true));
    }

    @Test
    public void givenUrl_whenSuccessOnPutUpdatesTask_thenCorrect() {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            Task updatedTask = optionalTask.get();
            updatedTask.setDescription("Tarefa atualizada");

            given()
                .contentType(ContentType.JSON)
                .body(updatedTask)
            .when()
                .put("/taskEdit/" + taskId)
            .then()
                .statusCode(200)
                .body("description", equalTo("Tarefa atualizada"));
        }
    }

    @Test
    public void givenUrl_whenFailureOnGetTaskById_thenNotFound() {
        when()
            .get("/task/9999")
        .then()
            .statusCode(404)
            .log().ifValidationFails();
    }

    @Test
    public void givenUrl_whenSuccessOnDeleteRemovesTask_thenCorrect() {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isPresent()) {
            when()
                .delete("/taskRemove/" + taskId)
            .then()
                .statusCode(204)
                .log().ifValidationFails();
        }
    }

    @Test
    public void givenUrl_whenSuccessOnGetTasksByPriority_thenCorrect() {
        when()
            .get("/taskPriority/MEDIUM")
        .then()
            .statusCode(200)
            .log().ifValidationFails();
    }

    @Test
    public void givenUrl_whenSuccessOnGetTasksByStatus_thenCorrect() {
        when()
            .get("/taskStatus/PENDING")
        .then()
            .statusCode(200)
            .log().ifValidationFails();
    }
}
