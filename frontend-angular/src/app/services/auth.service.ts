// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

interface JwtPayload {
  userId: number;
  exp: number;
  role?: string;
  [key: string]: any;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private loginUrl = 'http://localhost:8085/api/users/login';

  constructor(private http: HttpClient, private router: Router) {}

  // ⚠️ Supprimé le console.log pour ne pas afficher les identifiants
  login(username: string, password: string): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(this.loginUrl, { username, password });
  }

  saveToken(token: string): void {
    localStorage.setItem("jwt_token", token);
  }

  getToken(): string | null {
    if (typeof window !== 'undefined' && window.localStorage) {
      return localStorage.getItem('jwt_token');
    }
    return null;
  }

  removeToken(): void {
    localStorage.removeItem("jwt_token");
  }

  logout(): void {
    this.removeToken();
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const payload: JwtPayload = jwtDecode(token);
      const now = Math.floor(Date.now() / 1000);
      return payload.exp > now;
    } catch {
      return false;
    }
  }

  getUserIdFromToken(): number {
    const token = this.getToken();
    if (!token) return 0;

    try {
      const payload: JwtPayload = jwtDecode(token);
      return payload.userId || 0;
    } catch {
      return 0;
    }
  }

  getRoleFromToken(): string | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload: JwtPayload = jwtDecode(token);
      return payload.role || null;
    } catch {
      return null;
    }
  }
}
