import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  login(username: string, password: string) {
    // The server response is a plain string, not an object with a token property
    return this.http.post<string>('http://localhost:8081/api/user/authenticate', { "username": username, "password": password })
      .pipe(tap(token => {
        if (token) {
          this.storeToken(token);
        }
      }));
  }

  private storeToken(token: string) {
    localStorage.setItem('auth_token', token);
  }
}
