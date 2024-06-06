package com.labdessoft.roteiro01.unit.entity;

import com.labdessoft.roteiro01.entity.Priority;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriorityTest {

    @Test
    void testPriorityValues() {
        assertEquals(Priority.HIGH, Priority.valueOf("HIGH"));
        assertEquals(Priority.MEDIUM, Priority.valueOf("MEDIUM"));
        assertEquals(Priority.LOW, Priority.valueOf("LOW"));
    }

    @Test
    void testPriorityEnumCount() {
        assertEquals(3, Priority.values().length);
    }
}
