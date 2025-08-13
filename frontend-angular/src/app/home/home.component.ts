// src/app/home/home.component.ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
	standalone: false,

  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
  constructor(private router: Router) {}

   allerVersInscription() {
    console.log('Tentative de navigation vers /register');
    this.router.navigate(['/register']).then(success => {
      if (success) {
        console.log('Navigation vers /register réussie');
      } else {
        console.error('Navigation vers /register échouée');
      }
    }).catch(err => {
      console.error('Erreur lors de la navigation :', err);
    });
  }
  allerVersConnexion() {
      this.router.navigate(['/login']); // ← redirection vers /login
    }
}
