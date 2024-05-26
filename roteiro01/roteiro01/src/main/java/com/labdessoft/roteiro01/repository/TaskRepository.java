package com.labdessoft.roteiro01.repository;

import com.labdessoft.roteiro01.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDueDate(LocalDate dueDate);

    @Query("SELECT t FROM Task t WHERE t.dueDate = :date OR (t.dueDate IS NULL AND :date = CURRENT_DATE + t.deadlineInDays)")
    List<Task> findTasksByDueDateOrDeadlineInDays(@Param("date") LocalDate date);
}
