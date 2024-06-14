package com.labdessoft.roteiro01.mock;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.enums.Priority;
import com.labdessoft.roteiro01.enums.TaskType;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TaskMock {

    public static List<Task> createTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setDescription("Task 1");
        task1.setCompleted(false);
        task1.setDueDate(LocalDate.now().plusDays(5));
        task1.setTaskType(TaskType.DATE);
        task1.setStatus("Scheduled");
        task1.setPriority(Priority.HIGH);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setDescription("Task 2");
        task2.setCompleted(true);
        task2.setDueDate(LocalDate.now().plusDays(10));
        task2.setDeadlineInDays(7);
        task2.setTaskType(TaskType.DEADLINE);
        task2.setStatus("Completed");
        task2.setPriority(Priority.MEDIUM);

        return Arrays.asList(task1, task2);
    }

    public static Task createTask(int id) {
        Task task = new Task();
        task.setId((long) id);
        task.setDescription("Task " + id);
        task.setCompleted(false);
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setTaskType(TaskType.DATE);
        task.setStatus("Scheduled");
        task.setPriority(Priority.HIGH);
        return task;
    }
}
