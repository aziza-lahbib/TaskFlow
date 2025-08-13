import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { ProfilComponent } from './profil/profil.component';
import { AuthGuard } from './guards/auth.guard';
import { RegisterComponent } from './register/register.component'; // importe le composant
import { ProfilAdminComponent } from './profil-admin/profil-admin.component';
const routes: Routes = [
  
  { path: '', redirectTo: '/home', pathMatch: 'full' },  // ✅ redirection par défaut vers /home
  { path: 'login', component: LoginComponent },
  { path: 'home', component: HomeComponent },  // ✅ assure-toi d’avoir ce composant

  { path: 'profil', component: ProfilComponent, canActivate: [AuthGuard] },
  // ✅ toute route inconnue va vers /home
  { path: 'register', component: RegisterComponent } , // 👈 ta route vers la page d'inscription
  { path: 'profilAdmin', component: ProfilAdminComponent },

];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
