import React, { useState } from "react";
import { TodoForm } from "./TodoForm";
import { TodoList } from "./TodoList";
import { v4 as uuidv4 } from "uuid";

export const TodoWrapper = () => {
  const [todos, setTodos] = useState([]);
  const [editTask, setEditTask] = useState(null);

  const addTodo = (description) => {
    setTodos([...todos, { id: uuidv4(), description, completed: false }]);
  };

  const deleteTodo = (id) => {
    setTodos(todos.filter((todo) => todo.id !== id));
  };

  const editTodo = (id) => {
    const todoToEdit = todos.find((todo) => todo.id === id);
    setEditTask(todoToEdit);
  };

  const updateTodo = (id, updatedDescription) => {
    setTodos(
      todos.map((todo) =>
        todo.id === id ? { ...todo, description: updatedDescription } : todo
      )
    );
    setEditTask(null);
  };

  return (
    <div className="TodoWrapper">
      <h1>Lista de Tarefas</h1>
      {editTask ? (
        <TodoForm
          addTodo={(description) => updateTodo(editTask.id, description)}
          initialValue={editTask.description}
        />
      ) : (
        <TodoForm addTodo={addTodo} />
      )}
      {todos.map((todo) => (
        <TodoList
          key={todo.id}
          task={todo}
          deleteTodo={deleteTodo}
          editTodo={editTodo}
        />
      ))}
    </div>
  );
};
