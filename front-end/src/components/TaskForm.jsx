import React, { useState } from 'react';
import TaskService from './TaskService';
import './style.css';

const TaskForm = () => {
    const [description, setDescription] = useState('');
    const [type, setType] = useState('DATE');
    const [dueDate, setDueDate] = useState('');
    const [priority, setPriority] = useState('LOW');
    const [error, setError] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const task = {
                description,
                taskType: type,
                dueDate: type === 'DATE' ? dueDate : null,
                priority,
            };
            await TaskService.createTask(task);
            setDescription('');
            setType('DATE');
            setDueDate('');
            setPriority('LOW');
            setError('');
        } catch (err) {
            setError('Erro: Não foi possível criar a tarefa');
        }
    };

    return (
        <div className="form-container">
            <h2>Criar Nova Tarefa</h2>
            {error && <div className="error">{error}</div>}
            <form onSubmit={handleSubmit}>
                <label>Descrição:</label>
                <input
                    type="text"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                />
                <label>Tipo:</label>
                <select value={type} onChange={(e) => setType(e.target.value)}>
                    <option value="DATE">Data</option>
                    <option value="DEADLINE">Prazo</option>
                </select>
                <label>Data de Vencimento:</label>
                <input
                    type="date"
                    value={dueDate}
                    onChange={(e) => setDueDate(e.target.value)}
                    required={type === 'DATE'}
                    disabled={type !== 'DATE'}
                />
                <label>Prioridade:</label>
                <select value={priority} onChange={(e) => setPriority(e.target.value)}>
                    <option value="LOW">Baixa</option>
                    <option value="MEDIUM">Média</option>
                    <option value="HIGH">Alta</option>
                </select>
                <button type="submit">Criar Tarefa</button>
            </form>
        </div>
    );
};

export default TaskForm;
