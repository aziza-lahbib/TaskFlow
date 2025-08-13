import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { ProfilComponent } from './profil/profil.component';
import { RegisterComponent } from './register/register.component';
import { AuthInterceptor } from './interceptors/auth.interceptor'; // adapte le chemin si besoin
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ProfilAdminComponent } from './profil-admin/profil-admin.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent , // car standalone: false
    RegisterComponent,  //  ici

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,  // Obligatoire pour HTTP et intercepteur


  ],
  providers: [
    provideHttpClient(withFetch()),

    {
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptor,
    multi: true
  }
    
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
