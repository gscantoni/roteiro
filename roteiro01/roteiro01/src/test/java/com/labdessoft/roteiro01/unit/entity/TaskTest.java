package com.labdessoft.roteiro01.unit.entity;

import com.labdessoft.roteiro01.entity.Priority;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskConstructorWithFullParameters() {
        Task task = new Task("Task Title", "Task Description", true, LocalDate.now(), 5, Priority.HIGH, TaskType.DEADLINE);
        assertEquals("Task Title", task.getTitle());
        assertEquals("Task Description", task.getDescription());
        assertTrue(task.getCompleted());
        assertNotNull(task.getDueDate());
        assertEquals(5, task.getDeadlineInDays());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(TaskType.DEADLINE, task.getTaskType());
    }

    @Test
    void testTaskConstructorWithPartialParameters() {
        Task task = new Task("Task Title", "Task Description", Priority.LOW, TaskType.DATE, LocalDate.now());
        assertEquals("Task Title", task.getTitle());
        assertEquals("Task Description", task.getDescription());
        assertEquals(Priority.LOW, task.getPriority());
        assertEquals(TaskType.DATE, task.getTaskType());
        assertNotNull(task.getDueDate());
    }

    @Test
    void testDetermineStatusCompleted() {
        Task task = new Task();
        task.setCompleted(true);
        assertEquals("Concluída", task.determineStatus());
    }

    @Test
    void testDetermineStatusDateType() {
        Task task = new Task();
        task.setTaskType(TaskType.DATE);
        task.setDueDate(LocalDate.now().minusDays(1));
        assertTrue(task.determineStatus().contains("Atrasada"));
    }

    @Test
    void testDetermineStatusDeadlineType() {
        Task task = new Task();
        task.setTaskType(TaskType.DEADLINE);
        task.setDueDate(LocalDate.now());
        task.setDeadlineInDays(0);
        assertTrue(task.determineStatus().contains("Atrasada"));
    }

    @Test
    void testDetermineStatusFreeType() {
        Task task = new Task();
        task.setTaskType(TaskType.FREE);
        assertEquals("Prevista", task.determineStatus());
    }
}
