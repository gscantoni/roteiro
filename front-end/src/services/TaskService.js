import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const getTasks = () => axios.get(`${API_URL}/tasks`);
const getTask = (id) => axios.get(`${API_URL}/task/${id}`);
const createTask = (task) => axios.post(`${API_URL}/taskCreate`, task); // Corrigido aqui
const updateTask = (id, task) => axios.put(`${API_URL}/taskEdit/${id}`, task);
const completeTask = (id) => axios.put(`${API_URL}/complete/${id}`);
const deleteTask = (id) => axios.delete(`${API_URL}/taskRemove/${id}`);

const TaskService = {
  getTasks,
  getTask,
  createTask,
  updateTask,
  completeTask,
  deleteTask,
};

export default TaskService;
