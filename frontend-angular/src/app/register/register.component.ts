import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  standalone:false,
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  user = {
    username: '',
    password: '',
    email: '',
    departmentId: ''
  };

  departments: any[] = [];
  message: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.http.get<any[]>('http://localhost:8085/api/departments').subscribe(
      data => this.departments = data,
      error => console.error('Erreur chargement départements', error)
    );
  }

  onRegister(): void {
    this.http.post('http://localhost:8085/api/users/register', this.user).subscribe({
      next: () => {
        console.log('Inscription réussie');
        alert('Inscription réussie');
        this.router.navigate(['/login']);
      },
      error: (error) => {
        if (error.status === 409) {
          alert('Ce nom d’utilisateur ou email existe déjà.');
        } else {
          alert('Erreur lors de l’inscription.');
        }
        console.error('Erreur lors de l’inscription :', error);
      }
    });
  }
}
