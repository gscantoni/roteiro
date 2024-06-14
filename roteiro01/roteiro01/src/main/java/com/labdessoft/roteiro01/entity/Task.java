package com.labdessoft.roteiro01.entity;

import com.labdessoft.roteiro01.enums.Priority;
import com.labdessoft.roteiro01.enums.TaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Todos os detalhes sobre uma tarefa.")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Descrição da tarefa deve possuir pelo menos 10 caracteres")
    @Size(min = 10, message = "Descrição da tarefa deve possuir pelo menos 10 caracteres")
    private String description;

    @Schema(description = "Tarefa concluída ou não concluída")
    private Boolean completed;

    @Schema(description = "Vencimento da tarefa em data")
    private LocalDate dueDate;

    @Schema(description = "Vencimento da tarefa em dias")
    private Integer deadlineInDays;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo da tarefa - Data, prazo ou livre -")
    private TaskType taskType;

    @Schema(description = "Status da tarefa - Prevista, X dias de atraso ou concluída -")
    private String status;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Prioridade da tarefa - HIGH, MEDIUM, LOW -")
    private Priority priority;

    public Task(String description, Boolean completed, LocalDate dueDate, Integer deadlineInDays, TaskType taskType, String status, Priority priority) {
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
        this.deadlineInDays = deadlineInDays;
        this.taskType = taskType;
        this.status = status;
        this.priority = priority;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getDeadlineInDays() {
        return deadlineInDays;
    }

    public void setDeadlineInDays(Integer deadlineInDays) {
        this.deadlineInDays = deadlineInDays;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String determineStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", description=" + description + ", completed=" + completed + ", status=" + status + ", priority=" + priority + "]";
    }
}
