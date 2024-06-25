import React, { useState } from 'react';
import TaskService from '../services/TaskService';

const CreateTask = () => {
  const [description, setDescription] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [deadlineInDays, setDeadlineInDays] = useState('');
  const [taskType, setTaskType] = useState('DATE');
  const [priority, setPriority] = useState('MEDIUM');
  const [error, setError] = useState(null);

  const handleSubmit = (e) => {
    e.preventDefault();

    if (description.length < 4) {
      setError("Descrição deve ter pelo menos 10 caracteres.");
      return;
    }

    if (taskType === 'DATE' && !dueDate) {
      setError("Data de vencimento é obrigatória para tarefas do tipo 'DATE'.");
      return;
    }

    if (taskType === 'DEADLINE' && !deadlineInDays) {
      setError("Prazo em dias é obrigatório para tarefas do tipo 'DEADLINE'.");
      return;
    }

    const newTask = {
      description,
      dueDate: taskType === 'DATE' ? dueDate : null,
      deadlineInDays: taskType === 'DEADLINE' ? parseInt(deadlineInDays) : null,
      taskType,
      priority,
      completed: false,
      status: 'Prevista'
    };

    console.log(newTask); 

    TaskService.createTask(newTask)
      .then(response => {
        alert('Tarefa criada com sucesso!');
        setDescription('');
        setDueDate('');
        setDeadlineInDays('');
        setTaskType('DATE');
        setPriority('MEDIUM');
        setError(null);
      })
      .catch(error => setError(error.message));
  };

  return (
    <div>
      <h2>Criar Nova Tarefa</h2>
      {error && <div>Error: {error}</div>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Descrição:</label>
          <input type="text" value={description} onChange={(e) => setDescription(e.target.value)} required />
        </div>
        <div>
          <label>Tipo:</label>
          <select value={taskType} onChange={(e) => setTaskType(e.target.value)}>
            <option value="DATE">Data</option>
            <option value="DEADLINE">Prazo</option>
            <option value="FREE">Livre</option>
          </select>
        </div>
        {taskType === 'DATE' && (
          <div>
            <label>Data de Vencimento:</label>
            <input type="date" value={dueDate} onChange={(e) => setDueDate(e.target.value)} required />
          </div>
        )}
        {taskType === 'DEADLINE' && (
          <div>
            <label>Prazo em Dias:</label>
            <input type="number" value={deadlineInDays} onChange={(e) => setDeadlineInDays(e.target.value)} required />
          </div>
        )}
        <div>
          <label>Prioridade:</label>
          <select value={priority} onChange={(e) => setPriority(e.target.value)}>
            <option value="HIGH">Alta</option>
            <option value="MEDIUM">Média</option>
            <option value="LOW">Baixa</option>
          </select>
        </div>
        <button type="submit">Criar Tarefa</button>
      </form>
    </div>
  );
};

export default CreateTask;
