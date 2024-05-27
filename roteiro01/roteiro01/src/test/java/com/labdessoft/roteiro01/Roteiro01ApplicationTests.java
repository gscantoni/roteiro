package com.labdessoft.roteiro01;

import com.labdessoft.roteiro01.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class Roteiro01ApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateTask() {
        Task task = new Task("Title", "Description", false, LocalDate.now().plusDays(1));
        assertThat(task.getTitle()).isEqualTo("Title");
        assertThat(task.getDescription()).isEqualTo("Description");
        assertThat(task.getCompleted()).isFalse();
        assertThat(task.getDueDate()).isEqualTo(LocalDate.now().plusDays(1));
    }

    @Test
    void testTaskStatus() {
        Task task = new Task("Title", "Description", false, LocalDate.now().minusDays(1));
        assertThat(task.getStatus()).contains("dias de atraso");

        task.setCompleted(true);
        assertThat(task.getStatus()).isEqualTo("Concluída");
    }
}
