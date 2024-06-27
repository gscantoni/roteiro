package com.labdessoft.roteiro01.unit.repository;

import com.labdessoft.roteiro01.entity.Task;
import com.labdessoft.roteiro01.enums.Priority;
import com.labdessoft.roteiro01.enums.TaskType;
import com.labdessoft.roteiro01.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Transactional
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void limparBancoDeDados() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar uma tarefa no banco de dados")
    void testSalvarTarefa() {
        // Configuração
        Task task = new Task();
        task.setDescription("Tarefa de Teste");
        task.setTaskType(TaskType.DATE);
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);
        task.setPriority(Priority.HIGH);

        // Execução
        Task tarefaSalva = taskRepository.save(task);

        // Verificação
        assertNotNull(tarefaSalva);
        assertNotNull(tarefaSalva.getId());
        assertEquals(task.getDescription(), tarefaSalva.getDescription());
        assertEquals(task.getTaskType(), tarefaSalva.getTaskType());
        assertEquals(task.getDueDate(), tarefaSalva.getDueDate());
        assertEquals(task.getCompleted(), tarefaSalva.getCompleted());
        assertEquals(task.getPriority(), tarefaSalva.getPriority());
    }

    @Test
    @DisplayName("Deve recuperar uma tarefa pelo ID")
    void testRecuperarTarefaPorId() {
        // Configuração
        Task task = new Task();
        task.setDescription("Tarefa de Teste");
        task.setTaskType(TaskType.DATE);
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);
        task.setPriority(Priority.HIGH);
        Task tarefaSalva = taskRepository.save(task);

        // Execução
        Optional<Task> tarefaRecuperadaOptional = taskRepository.findById(tarefaSalva.getId());

        // Verificação
        assertTrue(tarefaRecuperadaOptional.isPresent());
        Task tarefaRecuperada = tarefaRecuperadaOptional.get();
        assertEquals(tarefaSalva.getId(), tarefaRecuperada.getId());
        assertEquals(tarefaSalva.getDescription(), tarefaRecuperada.getDescription());
        assertEquals(tarefaRecuperada.getTaskType(), tarefaRecuperada.getTaskType());
        assertEquals(tarefaRecuperada.getDueDate(), tarefaRecuperada.getDueDate());
        assertEquals(tarefaRecuperada.getCompleted(), tarefaRecuperada.getCompleted());
        assertEquals(tarefaRecuperada.getPriority(), tarefaRecuperada.getPriority());
    }

    @Test
    @DisplayName("Deve recuperar todas as tarefas")
    void testRecuperarTodasTarefas() {
        // Configuração
        Task tarefa1 = new Task();
        tarefa1.setDescription("Tarefa 1");
        tarefa1.setTaskType(TaskType.DATE);
        tarefa1.setDueDate(LocalDate.now().plusDays(5));
        tarefa1.setCompleted(false);
        tarefa1.setPriority(Priority.MEDIUM);
        Task tarefa2 = new Task();
        tarefa2.setDescription("Tarefa 2");
        tarefa2.setTaskType(TaskType.DATE);
        tarefa2.setDueDate(LocalDate.now().plusDays(7));
        tarefa2.setCompleted(true);
        tarefa2.setPriority(Priority.HIGH);
        taskRepository.save(tarefa1);
        taskRepository.save(tarefa2);

        // Execução
        List<Task> todasTarefas = taskRepository.findAll();

        // Verificação
        assertEquals(2, todasTarefas.size());
    }

    @Test
    @DisplayName("Deve excluir uma tarefa pelo ID")
    void testExcluirTarefaPorId() {
        // Configuração
        Task task = new Task();
        task.setDescription("Tarefa de Teste");
        task.setTaskType(TaskType.DATE);
        task.setDueDate(LocalDate.now().plusDays(5));
        task.setCompleted(false);
        task.setPriority(Priority.HIGH);
        Task tarefaSalva = taskRepository.save(task);

        // Execução
        taskRepository.deleteById(tarefaSalva.getId());

        // Verificação
        assertFalse(taskRepository.existsById(tarefaSalva.getId()));
    }

    @Test
    @DisplayName("Deve encontrar tarefas por prioridade")
    void testEncontrarTarefasPorPrioridade() {
        // Configuração
        Task tarefa1 = new Task();
        tarefa1.setDescription("Tarefa 1");
        tarefa1.setTaskType(TaskType.DATE);
        tarefa1.setDueDate(LocalDate.now().plusDays(5));
        tarefa1.setCompleted(false);
        tarefa1.setPriority(Priority.HIGH);
        Task tarefa2 = new Task();
        tarefa2.setDescription("Tarefa 2");
        tarefa2.setTaskType(TaskType.DATE);
        tarefa2.setDueDate(LocalDate.now().plusDays(7));
        tarefa2.setCompleted(true);
        tarefa2.setPriority(Priority.MEDIUM);
        taskRepository.save(tarefa1);
        taskRepository.save(tarefa2);

        // Execução
        List<Task> tarefasAltaPrioridade = taskRepository.findByPriority(Priority.HIGH);

        // Verificação
        assertEquals(1, tarefasAltaPrioridade.size());
        assertEquals(Priority.HIGH, tarefasAltaPrioridade.get(0).getPriority());
    }

    @Test
    @DisplayName("Deve encontrar tarefas por status")
    void testEncontrarTarefasPorStatus() {
        // Configuração
        Task tarefa1 = new Task();
        tarefa1.setDescription("Tarefa 1");
        tarefa1.setTaskType(TaskType.DATE);
        tarefa1.setDueDate(LocalDate.now().plusDays(5));
        tarefa1.setCompleted(false);
        tarefa1.setPriority(Priority.HIGH);
        tarefa1.setStatus("Scheduled");
        Task tarefa2 = new Task();
        tarefa2.setDescription("Tarefa 2");
        tarefa2.setTaskType(TaskType.DATE);
        tarefa2.setDueDate(LocalDate.now().plusDays(7));
        tarefa2.setCompleted(true);
        tarefa2.setPriority(Priority.MEDIUM);
        tarefa2.setStatus("Completed");
        taskRepository.save(tarefa1);
        taskRepository.save(tarefa2);

        // Execução
        List<Task> tarefasScheduled = taskRepository.findByStatus("Scheduled");

        // Verificação
        assertEquals(1, tarefasScheduled.size());
        assertEquals("Scheduled", tarefasScheduled.get(0).getStatus());
    }
}
