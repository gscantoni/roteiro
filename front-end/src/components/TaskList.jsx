import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import TaskService from '../services/TaskService';

const TaskList = () => {
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    TaskService.getTasks()
      .then(response => setTasks(response.data))
      .catch(error => setError(error.message));
  }, []);

  if (error) return <div>Error: {error}</div>;

  return (
    <div>
      <h1>Tarefas</h1>
      <Link to="/create">Criar Nova Tarefa</Link>
      <ul>
        {tasks.map(task => (
          <li key={task.id}>
            {task.description} - {task.status} - {task.priority}
            <Link to={`/edit/${task.id}`}>Editar</Link>
            <Link to={`/complete/${task.id}`}>Concluir</Link>
            <Link to={`/delete/${task.id}`}>Remover</Link>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default TaskList;
