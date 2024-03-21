import {Injectable} from '@angular/core';
import {HttpService} from './http.service';
import {Observable, of} from 'rxjs';
import {User} from "./users-dashboard/user.model";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private httpService: HttpService) {
  }

  public saveUser(user: User): Observable<any> {
    return this.httpService.post(user, 'api/user/save');
  }

  public modifyUser(user: User): Observable<any> {
    return this.httpService.put(user, 'api/user/modify');
  }

  public getAllUsers(): Observable<any> {
    return this.httpService.get('api/user/list');
  }

  public deleteUser(username: string): Observable<any> {
    return this.httpService.delete(`api/user/delete/${username}`)
  }

  public login(username: string, password: string): Observable<any> {
    return this.httpService.post({"username": username, "password": password}, 'api/user/authenticate');
  }

  public logout(): Observable<any> {
    const token = localStorage.getItem('auth_token');
    if (!token) {
      console.log('No auth token found. Clearing local storage.');
      localStorage.clear();
      return of('No token'); // Immediately return an observable
    }
  
    return this.httpService.post({}, 'api/logout');
  }

  

 
}
