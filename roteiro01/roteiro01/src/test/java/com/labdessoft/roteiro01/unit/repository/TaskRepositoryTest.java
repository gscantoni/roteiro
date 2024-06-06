package com.labdessoft.roteiro01.unit.repository;

import com.labdessoft.roteiro01.entity.Priority;
import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.entity.TaskType;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        Task task1 = new Task("Task 1", "Description 1", false, LocalDate.now(), null, Priority.HIGH, TaskType.DATE);
        Task task2 = new Task("Task 2", "Description 2", false, LocalDate.now().plusDays(1), 5, Priority.MEDIUM, TaskType.DEADLINE);
        Task task3 = new Task("Task 3", "Description 3", false, LocalDate.now().plusDays(2), null, Priority.LOW, TaskType.FREE);
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);
    }

    @Test
    void testFindByPriority() {
        List<Task> highPriorityTasks = taskRepository.findByPriority(Priority.HIGH);
        assertEquals(1, highPriorityTasks.size());
        assertEquals("Task 1", highPriorityTasks.get(0).getTitle());

        List<Task> mediumPriorityTasks = taskRepository.findByPriority(Priority.MEDIUM);
        assertEquals(1, mediumPriorityTasks.size());
        assertEquals("Task 2", mediumPriorityTasks.get(0).getTitle());

        List<Task> lowPriorityTasks = taskRepository.findByPriority(Priority.LOW);
        assertEquals(1, lowPriorityTasks.size());
        assertEquals("Task 3", lowPriorityTasks.get(0).getTitle());
    }

    @Test
    void testFindByStatus() {
        Task task = taskRepository.findAll().get(0);
        task.setStatus("In Progress");
        taskRepository.save(task);

        List<Task> inProgressTasks = taskRepository.findByStatus("In Progress");
        assertEquals(1, inProgressTasks.size());
        assertEquals("Task 1", inProgressTasks.get(0).getTitle());
    }
}
