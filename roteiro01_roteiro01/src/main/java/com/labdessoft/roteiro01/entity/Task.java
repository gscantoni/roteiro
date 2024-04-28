package com.labdessoft.roteiro01.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
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

    @Schema(description = "O título da tarefa")
    @NotBlank(message = "O título não pode estar em branco")
    @Size(min = 3, message = "O título deve ter pelo menos 3 caracteres")
    private String title;

    @Schema(name = "Descrição da tarefa deve possuir pelo menos 10 caracteres")
    @Size(min = 10, message = "Descrição da tarefa deve possuir pelo menos 10 caracteres")
    private String description;

    @Schema(description = "Indica se a tarefa foi concluída")
    private Boolean completed;

    @Schema(description = "Data prevista de conclusão da tarefa")
    private LocalDate dueDate;

    @Schema(description = "Prazo em dias para conclusão da tarefa")
    private Integer deadlineInDays;
    
    @Schema(description = "Indica a prioridade")
    private Priority priority;
    
    @Schema(description = "Status atual da tarefa")
    private String status;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    
    public Integer getDeadlineInDays() { return deadlineInDays; } 
    public void setDeadlineInDays(Integer deadlineInDays) { this.deadlineInDays = deadlineInDays; } 
    
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }    
    
    public String getStatus() {
        if (completed) {
            return "Concluída";
        } else {
            LocalDate referenceDate = dueDate != null ? dueDate : (deadlineInDays != null ? LocalDate.now().plusDays(deadlineInDays) : null);
            if (referenceDate != null && referenceDate.isBefore(LocalDate.now())) {
                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(referenceDate, LocalDate.now());
                return daysBetween + " dias de atraso";
            } else {
                return "Prevista";
            }
        }
    }
    public void setStatus(String status) {
        this.status = status;
    }
    

    public Task(String title, String description, Boolean completed, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
    }

    public Task(String title, String description, Boolean completed, Integer deadlineInDays) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.deadlineInDays = deadlineInDays;
    }
    
    public Task(String title, String description, Boolean completed, LocalDate dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", title=" + title + ", description=" + description + ", completed=" + completed +
               ", dueDate=" + dueDate + ", deadlineInDays=" + deadlineInDays + "]";
    }
}
