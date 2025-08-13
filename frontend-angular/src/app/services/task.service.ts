import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

export interface Task {
  id: number;
  title: string;
  description: string;
  completed: boolean;
  validee?: number | null;  // 1 = validée, 0 = refusée, null = en attente
}

@Injectable({
  providedIn: 'root'
})
export class TaskService {
  private apiUrl = 'http://localhost:8085/api/tasks';

  constructor(private http: HttpClient) {}

  getTasksByUser(userId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/user/${userId}`).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Erreur lors du chargement des tâches :', error);
        return throwError(() => new Error('Erreur lors du chargement des tâches'));
      })
    );
  }

  updateTask(task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${task.id}`, task).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Erreur lors de la mise à jour de la tâche :', error);
        return throwError(() => new Error('Erreur lors de la mise à jour'));
      })
    );
  }

  deleteTask(taskId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${taskId}`).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Erreur lors de la suppression de la tâche :', error);
        return throwError(() => new Error('Erreur lors de la suppression'));
      })
    );
  }

  createTask(userId: number, task: Task): Observable<Task> {
    return this.http.post<Task>(`${this.apiUrl}/user/${userId}`, task).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Erreur lors de la création de la tâche :', error);
        return throwError(() => new Error('Erreur lors de la création'));
      })
    );
  }
}
