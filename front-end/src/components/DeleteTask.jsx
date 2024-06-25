import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import TaskService from '../services/TaskService';

const DeleteTask = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [error, setError] = useState(null);

  useEffect(() => {
    TaskService.deleteTask(id)
      .then(() => {
        alert('Tarefa removida com sucesso!');
        navigate('/');
      })
      .catch(error => setError(error.message));
  }, [id, navigate]);

  if (error) return <div>Error: {error}</div>;

  return <div>Removendo tarefa...</div>;
};

export default DeleteTask;
