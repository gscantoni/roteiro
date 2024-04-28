package com.labdessoft.roteiro01;

import com.labdessoft.roteiro01.entity.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Roteiro01ApplicationTests {

    @Autowired
    private Validator validator;

    @Test
    void carregaContexto() {
    }

    @Test
    void tarefaComDataFuturaDevePassarNaValidacao() {
        Task tarefa = new Task("Completar o projeto", "Deve ser concluído até o próximo mês", true, LocalDate.now().plusDays(30));
        Errors erros = new BeanPropertyBindingResult(tarefa, "tarefa");
        validator.validate(tarefa, erros);

        assertThat(erros.hasErrors()).isFalse();
    }

    @Test
    void tarefaComDataPassadaDeveFalharNaValidacao() {
        Task tarefa = new Task("Iniciar o projeto", "Isso deveria ter começado no mês passado", true, LocalDate.now().minusDays(30));
        Errors erros = new BeanPropertyBindingResult(tarefa, "tarefa");
        validator.validate(tarefa, erros);

        assertThat(erros.hasErrors()).isTrue();
        assertThat(erros.getFieldError("dueDate").getDefaultMessage()).isEqualTo("A data prevista de conclusão deve ser hoje ou no futuro");
    }
}
