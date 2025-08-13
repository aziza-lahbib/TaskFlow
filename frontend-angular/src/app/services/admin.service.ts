import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8085/api';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token'); // Toujours le même nom clé 'jwt_token'
    let headers = new HttpHeaders();

    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }

    return headers;
  }

  getDepartements(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/departments`, { headers: this.getHeaders() });
  }

  getUtilisateursByDepartement(departementId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/departments/${departementId}/users`, { headers: this.getHeaders() });
  }

  creerDepartement(departement: { name: string }): Observable<any> {
    const headers = this.getHeaders().set('Content-Type', 'application/json');
    return this.http.post<any>(`${this.apiUrl}/departments`, departement, { headers });
  }

  getTachesByUser(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/tasks/user/${userId}`, { headers: this.getHeaders() });
  }

  validerTaches(ids: number[]): Observable<any> {
    const headers = this.getHeaders().set('Content-Type', 'application/json');
    return this.http.post(`${this.apiUrl}/tasks/validate`, ids, { headers, responseType: 'text' });
  }

  refuserTaches(ids: number[]): Observable<any> {
    const headers = this.getHeaders().set('Content-Type', 'application/json');
    return this.http.post(`${this.apiUrl}/tasks/refuse`, ids, { headers, responseType: 'text' });
  }
}
