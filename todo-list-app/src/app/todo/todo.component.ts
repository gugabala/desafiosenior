// todo.component.ts

import { Component, OnInit } from '@angular/core';
import { TodoService } from '../todo.service';
import * as moment from 'moment'; // Importar moment.js

interface Task {
  id?: number;
  titulo: string;
  descricao: string;
  dataCriacao: string;
  dataConclusao?: string;
  status: 'PENDENTE' | 'CONCLUIDA';
  editing?: boolean;
}

@Component({
  selector: 'app-todo',
  templateUrl: './todo.component.html',
  styleUrls: ['./todo.component.css']
})
export class TodoComponent implements OnInit {
  tasks: Task[] = [];
  newTask: { titulo: string; descricao: string } = { titulo: '', descricao: '' };

  constructor(private todoService: TodoService) { }

  ngOnInit() {
    this.fetchTasks();
  }

  fetchTasks() {
    this.todoService.getTasks().subscribe((tasks: Task[]) => {
      this.tasks = tasks.map(task => ({ ...task, editing: false }));
    });
  }

  addTask() {
    if (this.newTask.titulo.trim().length > 0 && this.newTask.descricao.trim().length > 0) {
      const newTask: Task = {
        titulo: this.newTask.titulo,
        descricao: this.newTask.descricao,
        dataCriacao: moment().format('YYYY-MM-DD HH:mm:ss'), // Formatar data
        status: 'PENDENTE'
      };

      this.todoService.createTask(newTask).subscribe(() => {
        this.fetchTasks(); // Recarregar tarefas após adicionar nova
        this.newTask = { titulo: '', descricao: '' };
      });
    }
  }

  removeTask(index: number) {
    const task = this.tasks[index];
    if (task.id) {
      this.todoService.deleteTask(task.id).subscribe(() => {
        this.fetchTasks(); // Recarregar tarefas após exclusão
      });
    }
  }

  completeTask(index: number) {
    const task = this.tasks[index];
    if (task.id) {
      const dataConclusao =  moment().format('YYYY-MM-DD HH:mm:ss'); // Formatar data
      this.todoService.completeTask(task.id, dataConclusao).subscribe(() => {
        this.fetchTasks(); // Recarregar tarefas após concluir
      });
    }
  }

  editTask(index: number) {
    const task = this.tasks[index];
    if (task.status !== 'CONCLUIDA') {
      this.tasks[index].editing = true;
    }
  }

  updateTask(index: number, titulo: string, descricao: string) {
    const task = this.tasks[index];
    if (task.id) {
      const updatedTask: Partial<Task> = {
        titulo: titulo.trim(),
        descricao: descricao.trim(),
        dataConclusao: task.dataConclusao,
        status: task.status
      };
      this.todoService.updateTask(task.id, updatedTask).subscribe(() => {
        this.fetchTasks(); // Recarregar tarefas após atualização
      });
    }
    this.tasks[index].editing = false;
  }

  cancelEdit(index: number) {
    this.tasks[index].editing = false;
  }
}