# TaskFlow - Application Web de Gestion des Tâches

## Présentation du projet  
**TaskFlow** est une application web développée avec **Spring Boot** (Back-end) et **Angular** (Front-end) pour la gestion collaborative des tâches.  
Chaque utilisateur est rattaché à un département et peut créer, modifier, terminer ses tâches. Un administrateur valide ou refuse ces tâches.

---

## Acteurs du système

### Administrateur  
- Gérer les départements (création, modification, suppression)  
- Gérer les utilisateurs par département  
- Consulter et valider/refuser les tâches des utilisateurs  

### Utilisateur  
- S’inscrire et se connecter  
- Créer, modifier, supprimer ses tâches  
- Marquer les tâches comme terminées  
- Suivre le statut de validation des tâches  

---

## Fonctionnalités

### Côté Administrateur  
- Authentification sécurisée  
- Gestion des départements et utilisateurs  
- Consultation des tâches et validation/refus  

### Côté Utilisateur  
- Inscription / Connexion  
- Gestion personnelle des tâches (CRUD)  
- Marquage des tâches comme terminées  
- Suivi du statut de validation  

---

## Technologies utilisées

### Back-end  
- Java 17  
- Spring Boot (Spring MVC, Spring Data JPA, Spring Security)  
- MySQL (base de données)  
- Maven (gestion de projet)  

### Front-end  
- Angular 16+  
- TypeScript  
- HTML / CSS  

---

## Modules et dépendances Angular

- `BrowserModule`  
- `FormsModule` et `ReactiveFormsModule`  
- `HttpClientModule`  
- `AuthInterceptor` (gestion JWT)  
- `AuthGuard` (protection des routes)  

---

## Routage (app-routing.module.ts)

| Route          | Composant            | Protection           |
|----------------|----------------------|---------------------|
| `/` (vide)     | Redirection vers `/home` | -                  |
| `/home`        | HomeComponent        | Public              |
| `/login`       | LoginComponent       | Public              |
| `/register`    | RegisterComponent    | Public              |
| `/profil`      | ProfilComponent      | Protégé (AuthGuard) |
| `/profilAdmin` | ProfilAdminComponent | Protégé (AuthGuard)|

---

## Structure des composants

- **HomeComponent** : Page d'accueil  
- **LoginComponent** : Formulaire de connexion  
- **RegisterComponent** : Formulaire d'inscription  
- **ProfilComponent** : Gestion des tâches utilisateur (protégé)  
- **ProfilAdminComponent** : Interface d'administration  
- Services Angular pour la communication HTTP avec le backend  

---

## Architecture

L’application suit une architecture **client-serveur** :  
**[Utilisateur / Navigateur] ⇄ [Angular Front-end] ⇄ [Spring Boot Back-end] ⇄ [MySQL Database]**

---

## Installation et exécution

### 1. Cloner le projet  
```bash
git clone https://github.com/votre-utilisateur/TaskFlow.git

### 2. Backend 
cd backend
mvn clean install
mvn spring-boot:run

Configuration base de données dans application.properties :

spring.datasource.url=jdbc:mysql://localhost:3306/taskflow
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update

### 3. Frontend

cd frontend
npm install
ng serve

L’application sera disponible sur : http://localhost:4200
Lancer le serveur de développement Angular : ng serve








