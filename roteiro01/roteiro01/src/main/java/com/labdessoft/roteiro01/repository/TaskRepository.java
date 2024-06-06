package com.labdessoft.roteiro01.repository;

import com.labdessoft.roteiro01.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
