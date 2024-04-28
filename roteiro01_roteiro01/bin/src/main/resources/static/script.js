document.addEventListener('DOMContentLoaded', function() {
    var modal = document.getElementById("addTaskModal");
    var openModalBtn = document.getElementById("openModalBtn");
    var closeModalSpan = document.getElementsByClassName("closeBtn")[0];
    var form = document.getElementById('addTaskForm');

    openModalBtn.onclick = function() {
        openAddTaskModal();
    }

    closeModalSpan.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        const taskId = this.getAttribute('data-task-id');
        const title = document.getElementById('addTitle').value;
        const description = document.getElementById('addDescription').value;

        if (taskId) {
            updateTask(taskId, { title, description });
        } else {
            addTask({ title, description });
        }

        modal.style.display = "none";
        this.removeAttribute('data-task-id');
    });

    loadTasks();
});

function openAddTaskModal(task = null) {
    const modal = document.getElementById("addTaskModal");
    const titleInput = document.getElementById('addTitle');
    const descriptionInput = document.getElementById('addDescription');
    const completedCheckbox = document.getElementById('addCompleted');
    const form = document.getElementById('addTaskForm');

    titleInput.value = task ? task.title : '';
    descriptionInput.value = task ? task.description : '';
    completedCheckbox.checked = task ? task.completed : false;

    if (task) {
        form.setAttribute('data-task-id', task.id);
    } else {
        form.removeAttribute('data-task-id');
    }

    modal.style.display = "block";
}

function loadTasks() {
    fetch('/api/task')
        .then(response => response.json())
        .then(data => displayTasks(data))
        .catch(error => console.error('Erro ao buscar tarefas:', error));
    const tasks = [{id: 1, title: "Tarefa Exemplo", description: "Descrição exemplo", completed: false}];
    displayTasks(tasks);
}

function addTask(task) {
    fetch('/api/task', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(task)
    })
        .then(response => {
            if (response.ok) {
                loadTasks();
            } else {
                throw new Error('Erro ao adicionar tarefa');
            }
        })
        .catch(error => console.error('Erro ao adicionar tarefa:', error));
    loadTasks();
}

function updateTask(taskId, task) {
    const title = document.getElementById('addTitle').value;
    const description = document.getElementById('addDescription').value;

    fetch(`/api/task/${taskId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ title, description })
    })
        .then(response => {
            if (response.ok) {
                loadTasks();
            } else {
                alert('Erro ao editar tarefa');
            }
        })
        .catch(error => console.error('Erro ao editar tarefa:', error));
    loadTasks();
}

function displayTasks(tasks) {
    const taskList = document.getElementById('taskList');
    taskList.innerHTML = '';

    tasks.forEach(task => {
        const li = document.createElement('li');
        li.className = task.completed ? 'completed' : '';

        const checkBox = document.createElement('input');
        checkBox.type = 'checkbox';
        checkBox.checked = task.completed;
        checkBox.onchange = function() {
            updateTask(task.id, { title: task.title, description: task.description, completed: this.checked });
        };

        const title = document.createElement('span');
        title.textContent = task.title + ": ";

        const description = document.createTextNode(task.description);

        const editBtn = document.createElement('button');
        editBtn.innerHTML = '<i class="fas fa-pen"></i>';
        editBtn.onclick = function() {
            openAddTaskModal(task);
        };

        const deleteBtn = document.createElement('button');
        deleteBtn.innerHTML = '<i class="fas fa-trash"></i>';
        deleteBtn.onclick = function() {
            deleteTask(task.id);
        };

        li.appendChild(checkBox);
        li.appendChild(title);
        li.appendChild(description);
        li.appendChild(editBtn);
        li.appendChild(deleteBtn);

        taskList.appendChild(li);
        if (task.completed) {
            li.classList.add('completed');
        }
    });
}

function deleteTask(taskId) {
    fetch(`/api/task/${taskId}`, {
        method: 'DELETE'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao deletar tarefa');
            }
            loadTasks();
        })
        .catch(error => console.error('Erro ao deletar tarefa:', error));
    loadTasks();
}

