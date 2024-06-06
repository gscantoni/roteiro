package com.labdessoft.roteiro01.unit.entity;

import com.labdessoft.roteiro01.entity.TaskType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTypeTest {

    @Test
    void testTaskTypeValues() {
        assertEquals(TaskType.DATE, TaskType.valueOf("DATE"));
        assertEquals(TaskType.DEADLINE, TaskType.valueOf("DEADLINE"));
        assertEquals(TaskType.FREE, TaskType.valueOf("FREE"));
    }

    @Test
    void testTaskTypeEnumCount() {
        assertEquals(3, TaskType.values().length);
    }
}
