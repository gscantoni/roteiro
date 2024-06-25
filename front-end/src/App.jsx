import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import TaskList from './components/TaskList';
import CreateTask from './components/CreateTask';
import EditTask from './components/EditTask';
import CompleteTask from './components/CompleteTask';
import DeleteTask from './components/DeleteTask';

const App = () => (
  <Router>
    <div>
      <Routes>
        <Route path="/" element={<TaskList />} />
        <Route path="/create" element={<CreateTask />} />
        <Route path="/edit/:id" element={<EditTask />} />
        <Route path="/complete/:id" element={<CompleteTask />} />
        <Route path="/delete/:id" element={<DeleteTask />} />
      </Routes>
    </div>
  </Router>
);

export default App;
