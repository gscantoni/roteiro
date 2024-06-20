import React, { useState, useEffect } from 'react';

export const TodoForm = ({ addTodo, initialValue = '' }) => {
  const [value, setValue] = useState(initialValue);

  useEffect(() => {
    setValue(initialValue);
  }, [initialValue]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (value) {
      addTodo(value);
      setValue('');
    }
  };

  return (
    <form className="TodoForm" onSubmit={handleSubmit}>
      <input
        type="text"
        value={value}
        onChange={(e) => setValue(e.target.value)}
        className="todo-input"
        placeholder='Descrição da Tarefa'
      />
      <button type="submit" className='todo-btn'>
        {initialValue ? 'Atualizar Tarefa' : 'Adicionar Tarefa'}
      </button>
    </form>
  );
};
