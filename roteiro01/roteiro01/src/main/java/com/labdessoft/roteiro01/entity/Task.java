package com.labdessoft.roteiro01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @NotNull(message = "A prioridade não pode ser nula")
    private Priority priority;

    @Schema(description = "Status atual da tarefa")
    private String status;

    public Task(String title, String description, Boolean completed, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.dueDate = dueDate;
    }

    public String getStatus() {
        if (completed != null && completed) {
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
}
