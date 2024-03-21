import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UserDataDTO {
  username: string;
  email: string;
  phone: string;
  userType: string;
}

export interface UserModifyDTO {
  username: string;
  phone: string;
  email: string;
  newPassword: string;
  confirmNewPassword: string;
}

@Injectable({
  providedIn: 'root'
})


export class UsersService {
  private apiUrl = 'http://localhost:8081/api/user';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    let headers = new HttpHeaders();
    const token = localStorage.getItem('auth_token');
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    return headers;
  }

  getUserData(username: string): Observable<UserDataDTO> {
    return this.http.get<UserDataDTO>(`${this.apiUrl}/data/${username}`,  { headers: this.getHeaders() });
  }

  modifyUserOwnData(userDTO: UserModifyDTO): Observable<string> {
    return this.http.put(`${this.apiUrl}/modifymydata`, userDTO, { headers: this.getHeaders(), responseType: 'text' });
  }

}
