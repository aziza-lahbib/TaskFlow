import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  errorMsg = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    console.log('Début de la fonction onLogin()');

    if (!this.username || !this.password) {
      this.errorMsg = "Veuillez saisir un nom d'utilisateur et un mot de passe.";
      return;
    }

    this.authService.login(this.username, this.password).subscribe({
      next: (res) => {
        if (res.token) {
         this.authService.saveToken(res.token);

const role = this.authService.getRoleFromToken();
console.log("Rôle détecté :", role);

if (role === 'ADMIN') {
  this.router.navigate(['/profilAdmin']);
} else if (role === 'USER') {
  this.router.navigate(['/profil']);
} else {
  this.errorMsg = "Rôle inconnu. Accès refusé.";
}

        } else {
          this.errorMsg = "Réponse invalide du serveur.";
        }
      },
      error: (err) => {
        if (err.status === 0) {
          this.errorMsg = "Impossible de contacter le serveur.";
        } else if (err.status === 401) {
          this.errorMsg = "Nom d'utilisateur ou mot de passe incorrect";
        } else {
          this.errorMsg = "Une erreur est survenue.";
        }
      }
    });
  }
}
