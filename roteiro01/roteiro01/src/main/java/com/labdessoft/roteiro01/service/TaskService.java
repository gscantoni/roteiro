package com.labdessoft.roteiro01.service;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.enums.Priority;
import com.labdessoft.roteiro01.enums.TaskType;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public Task createTask(Task task) {
        validateTask(task);
        updateTaskStatus(task, LocalDate.now());
        return taskRepository.save(task);
    }

    public Task completeTask(long id) {
        Task task = getTaskById(id);
        task.setCompleted(true);
        updateTaskStatus(task, LocalDate.now());
        return taskRepository.save(task);
    }

    public Task updateTask(long id, Task taskDetails) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskDetails.setId(id);
        validateTask(taskDetails);
        updateTaskStatus(taskDetails, LocalDate.now());
        return taskRepository.save(taskDetails);
    }

    public boolean deleteTask(long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
        return true;
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return taskRepository.findByPriority(priority);
    }

    public List<Task> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }

    private void validateTask(Task task) {
        if (task.getTaskType() == null) {
            throw new IllegalArgumentException("Task type not specified.");
        }
        if (task.getDescription() == null || task.getDescription().length() < 10) {
            throw new IllegalArgumentException("Task description must be at least 10 characters long.");
        }
        if (task.getTaskType() == TaskType.DATE && (task.getDueDate() == null || task.getDueDate().isBefore(LocalDate.now()))) {
            throw new IllegalArgumentException("Due date must be equal or greater than the current date.");
        }
        if (task.getTaskType() == TaskType.DEADLINE && (task.getDeadlineInDays() == null || task.getDeadlineInDays() <= 0)) {
            throw new IllegalArgumentException("Deadline days must be greater than zero.");
        }
    }

    private void updateTaskStatus(Task task, LocalDate now) {
        switch (task.getTaskType()) {
            case DATE:
                if (!task.getCompleted()) {
                    if (task.getDueDate().isBefore(now)) {
                        long daysLate = ChronoUnit.DAYS.between(task.getDueDate(), now);
                        task.setStatus(daysLate + " days late");
                    } else {
                        task.setStatus("Scheduled");
                    }
                } else {
                    task.setStatus("Completed");
                }
                break;
            case DEADLINE:
                if (!task.getCompleted()) {
                    LocalDate dueDateWithExtension = task.getDueDate().plusDays(task.getDeadlineInDays());
                    if (now.isAfter(dueDateWithExtension)) {
                        long daysLate = ChronoUnit.DAYS.between(dueDateWithExtension, now);
                        task.setStatus(daysLate + " days late");
                    } else {
                        task.setStatus("Scheduled");
                    }
                } else {
                    task.setStatus("Completed");
                }
                break;
            case FREE:
                task.setStatus(task.getCompleted() ? "Completed" : "Scheduled");
                break;
            default:
                throw new IllegalArgumentException("Invalid task type: " + task.getTaskType());
        }
    }
}
