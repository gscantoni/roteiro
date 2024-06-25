import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import TaskService from '../services/TaskService';

const CompleteTask = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [error, setError] = useState(null);

  useEffect(() => {
    TaskService.completeTask(id)
      .then(() => {
        alert('Tarefa marcada como concluída!');
        navigate('/');
      })
      .catch(error => setError(error.message));
  }, [id, navigate]);

  if (error) return <div>Error: {error}</div>;

  return <div>Marcando tarefa como concluída...</div>;
};

export default CompleteTask;
