import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Task, TaskService } from '../services/task.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-profil',
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.css'],
  imports: [CommonModule, FormsModule]
})
export class ProfilComponent implements OnInit {
  user: any = null;
  tasks: Task[] = [];
  userId!: number;

  editingTaskId: number | null = null;
  editedTitle: string = '';
  editedDescription: string = '';

  newTaskTitle: string = '';
  newTaskDescription: string = '';

  loadingTasks: boolean = false;
  errorMessage: string = '';

  constructor(private http: HttpClient, private router: Router, private taskService: TaskService) {}

  ngOnInit(): void {
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get('http://localhost:8085/api/users/profile', { headers }).subscribe({
      next: (data: any) => {
        if (!data || !data.id) {
          this.logout();
          return;
        }
        this.user = data;
        this.userId = data.id;
        this.loadTasks();
      },
      error: (err) => {
        if (err.status === 401 || err.status === 403) {
          alert('Accès non autorisé.');
          this.logout();
        } else {
          this.errorMessage = 'Erreur lors du chargement du profil.';
        }
      }
    });
  }

  loadTasks(): void {
    if (!this.userId) return;
    this.loadingTasks = true;
    this.errorMessage = '';

    this.taskService.getTasksByUser(this.userId).subscribe({
      next: (tasks) => {
        this.tasks = tasks;
        this.loadingTasks = false;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des tâches :', err);
        this.errorMessage = 'Impossible de charger les tâches.';
        this.loadingTasks = false;
      }
    });
  }

  logout(): void {
    localStorage.removeItem('jwt_token');
    this.router.navigate(['/login']);
  }

  toggleTaskStatus(task: Task): void {
    // On interdit de changer le statut uniquement si validée (1) ou refusée (0)
    // Logique conservée pour empêcher toggling sur ces statuts
    if (task.validee === 1 || task.validee === 0) {
      return;
    }

    const previousStatus = task.completed;
    task.completed = !task.completed;

    this.taskService.updateTask(task).subscribe({
      next: (updatedTask) => {
        Object.assign(task, updatedTask);
      },
      error: (err) => {
        console.error('Erreur lors de la mise à jour', err);
        alert('Erreur lors de la mise à jour du statut.');
        task.completed = previousStatus;
      }
    });
  }

  editTask(task: Task): void {
    // Permet modification uniquement si NON validée
    if (task.validee === 1) {
      alert("Impossible de modifier une tâche validée.");
      return;
    }

    this.editingTaskId = task.id;
    this.editedTitle = task.title;
    this.editedDescription = task.description;
  }

  cancelEdit(): void {
    this.editingTaskId = null;
    this.editedTitle = '';
    this.editedDescription = '';
  }

  saveTask(task: Task): void {
    if (task.validee === 1) {
      alert("Impossible de modifier une tâche validée.");
      return;
    }

    const updatedTask: Task = {
      ...task,
      title: this.editedTitle.trim(),
      description: this.editedDescription.trim()
    };

    if (!updatedTask.title) {
      alert('Le titre ne peut pas être vide.');
      return;
    }

    this.taskService.updateTask(updatedTask).subscribe({
      next: (res) => {
        Object.assign(task, res);
        this.cancelEdit();
      },
      error: (err) => {
        console.error('Erreur lors de l’enregistrement', err);
        alert('Erreur lors de l’enregistrement de la tâche.');
      }
    });
  }

  deleteTask(taskId: number): void {
    // Suppression autorisée dans tous les cas, y compris validée ou refusée

    if (!confirm('Voulez-vous vraiment supprimer cette tâche ?')) return;

    this.taskService.deleteTask(taskId).subscribe({
      next: () => {
        this.tasks = this.tasks.filter(t => t.id !== taskId);
      },
      error: (err) => {
        console.error('Erreur lors de la suppression', err);
        alert('Erreur lors de la suppression de la tâche.');
      }
    });
  }

  addTask(): void {
    if (!this.userId) return;

    const title = this.newTaskTitle.trim();
    const description = this.newTaskDescription.trim();

    if (!title || !description) {
      alert('Veuillez remplir le titre et la description.');
      return;
    }

    const newTask: Task = {
      id: 0,
      title,
      description,
      completed: false,
      validee: null
    };

    this.taskService.createTask(this.userId, newTask).subscribe({
      next: (createdTask) => {
        this.tasks.push(createdTask);
        this.newTaskTitle = '';
        this.newTaskDescription = '';
      },
      error: (err) => {
        console.error('Erreur lors de l\'ajout de la tâche', err);
        alert('Erreur lors de l\'ajout de la tâche.');
      }
    });
  }
}
