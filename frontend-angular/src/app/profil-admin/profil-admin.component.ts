import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '../services/admin.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profil-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profil-admin.component.html',
  styleUrls: ['./profil-admin.component.css']
})
export class ProfilAdminComponent implements OnInit {
  departements: any[] = [];
  utilisateurs: any[] = [];
  selectedDepartementId: number | null = null;
  afficherFormulaire: boolean = false;
  nouveauDepartementNom: string = '';
  tachesUtilisateur: any[] = [];
  utilisateurSelectionne: any = null;

  constructor(private adminService: AdminService, private router: Router) {}

  ngOnInit(): void {
    const token = localStorage.getItem('jwt_token');
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }
    this.chargerDepartements();
  }

  chargerDepartements(): void {
    this.adminService.getDepartements().subscribe({
      next: (data) => this.departements = data,
      error: (err) => {
        console.error('Erreur chargement départements', err);
        if (err.status === 401 || err.status === 403) {
          alert("Accès non autorisé.");
          this.logout();
        }
      }
    });
  }
selectedDepartementNom: string | null = null;

  afficherUtilisateurs(id: number): void {
    this.selectedDepartementId = id;
    // Récupérer le nom du département sélectionné
  const dept = this.departements.find(dep => dep.id === id);
  this.selectedDepartementNom = dept ? dept.name : null;

    this.adminService.getUtilisateursByDepartement(id).subscribe({
      next: (data) => this.utilisateurs = data,
      error: (err) => {
        console.error('Erreur chargement utilisateurs', err);
        if (err.status === 401 || err.status === 403) {
          alert("Accès non autorisé.");
          this.logout();
        }
      }
    });
  }

  ouvrirFormulaireAjouter(): void {
    this.afficherFormulaire = true;
  }

  fermerFormulaire(): void {
    this.afficherFormulaire = false;
    this.nouveauDepartementNom = '';
  }

  ajouterDepartement(): void {
    if (!this.nouveauDepartementNom.trim()) {
      alert('Le nom du département est obligatoire.');
      return;
    }
    const departement = { name: this.nouveauDepartementNom.trim() };
    this.adminService.creerDepartement(departement).subscribe({
      next: (departementCree) => {
        this.departements.push(departementCree);
        this.fermerFormulaire();
      },
      error: (err) => {
        console.error('Erreur création département', err);
        alert('Erreur lors de la création du département.');
      }
    });
  }

  logout(): void {
    localStorage.removeItem('jwt_token');
    this.router.navigate(['/login']);
  }

  afficherTachesUtilisateur(user: any): void {
    this.utilisateurSelectionne = user;
    
    this.adminService.getTachesByUser(user.id).subscribe({
      next: (taches) => {
        // On ajoute selected à false pour chaque tâche
        this.tachesUtilisateur = taches.map(t => ({ ...t, selected: false }));
      },
      error: (err) => {
        console.error('Erreur chargement tâches', err);
        alert("Impossible de charger les tâches.");
        this.tachesUtilisateur = [];
      }
    });
  }

  validerTaches(): void {
  const selectedIds = this.tachesUtilisateur
    .filter(t => t.selected && t.completed)
    .map(t => t.id);

  if (selectedIds.length === 0) {
    alert("Veuillez sélectionner au moins une tâche terminée.");
    return;
  }

  this.adminService.validerTaches(selectedIds).subscribe({
    next: () => {
      alert("Tâches validées !");
      // Recharge depuis le backend pour avoir les données à jour
      this.tachesUtilisateur = this.tachesUtilisateur.map(t =>
  selectedIds.includes(t.id) ? { ...t, validee: 1, selected: false } : t
);

    },
    error: (err) => {
      console.error("Erreur validation tâches", err);
      alert("Erreur lors de la validation.");
    }
  });
}

refuserTaches(): void {
  const selectedIds = this.tachesUtilisateur
    .filter(t => t.selected && t.completed)
    .map(t => t.id);

  if (selectedIds.length === 0) {
    alert("Veuillez sélectionner au moins une tâche terminée.");
    return;
  }

  this.adminService.refuserTaches(selectedIds).subscribe({
    next: () => {
      alert("Tâches refusées !");
      // Recharge depuis le backend pour avoir les données à jour
     this.tachesUtilisateur = this.tachesUtilisateur.map(t =>
  selectedIds.includes(t.id) ? { ...t, validee: 0, selected: false } : t
);

    },
    error: (err) => {
      console.error("Erreur refus tâches", err);
      alert("Erreur lors du refus.");
    }
  });
}
}