import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';

const EditTask = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [task, setTask] = useState({
    description: '',
    dueDate: '',
    taskType: 'DATE',
    deadlineInDays: '',
    priority: 'MEDIUM',
    completed: false,
    status: 'Prevista'
  });
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchTask = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/task/${id}`);
        const data = response.data;
        setTask({
          description: data.description || '',
          dueDate: data.dueDate || '',
          taskType: data.taskType || 'DATE',
          deadlineInDays: data.deadlineInDays || '',
          priority: data.priority || 'MEDIUM',
          completed: data.completed || false,
          status: data.status || 'Prevista'
        });
      } catch (error) {
        setError('Error fetching task: ' + error.message);
      }
    };

    fetchTask();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setTask((prevTask) => ({
      ...prevTask,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.put(`http://localhost:8080/api/taskEdit/${id}`, task);
      navigate('/');
    } catch (error) {
      setError('Error updating task: ' + error.message);
    }
  };

  return (
    <div>
      <h2>Edit Task</h2>
      {error && <div>Error: {error}</div>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Description</label>
          <input
            type="text"
            name="description"
            value={task.description}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Due Date</label>
          <input
            type="date"
            name="dueDate"
            value={task.dueDate}
            onChange={handleChange}
            disabled={task.taskType !== 'DATE'}
          />
        </div>
        <div>
          <label>Task Type</label>
          <select
            name="taskType"
            value={task.taskType}
            onChange={handleChange}
          >
            <option value="DATE">Date</option>
            <option value="DEADLINE">Deadline</option>
            <option value="FREE">Free</option>
          </select>
        </div>
        {task.taskType === 'DEADLINE' && (
          <div>
            <label>Deadline in Days</label>
            <input
              type="number"
              name="deadlineInDays"
              value={task.deadlineInDays}
              onChange={handleChange}
              required
            />
          </div>
        )}
        <div>
          <label>Priority</label>
          <select
            name="priority"
            value={task.priority}
            onChange={handleChange}
          >
            <option value="HIGH">High</option>
            <option value="MEDIUM">Medium</option>
            <option value="LOW">Low</option>
          </select>
        </div>
        <button type="submit">Update Task</button>
      </form>
    </div>
  );
};

export default EditTask;
