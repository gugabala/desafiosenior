// todo.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface Task {
  id?: number;
  titulo: string;
  descricao: string;
  dataCriacao: string;
  dataConclusao?: string;
  status: 'PENDENTE' | 'CONCLUIDA';
}

@Injectable({
  providedIn: 'root'
})
export class TodoService {

  private apiUrl = 'http://localhost:8080/tarefas'; // Substitua pela URL da sua API

  constructor(private http: HttpClient) { }

  getTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.apiUrl);
  }

  createTask(task: Task): Observable<Task> {
    return this.http.post<Task>(this.apiUrl, task);
  }
  completeTask(id: number, dataConclusao: string): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/concluir`, { dataConclusao });
  }
  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateTask(id: number, task: Partial<Task>): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}`, task);
  }
}
