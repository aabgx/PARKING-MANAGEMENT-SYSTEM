import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../service/login.service';
import {DataService} from "../data.service";
import { User } from '../users-dashboard/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})

export class LoginComponent {
  username: string;
  password: string;

  constructor(private dataService: DataService, private router: Router) {
    this.username = ''; // initializing as empty string
    this.password = ''; // initializing as empty string
  }


  login() {
    this.dataService.login(this.username, this.password).subscribe({
      next: (token: string) => {
        console.log(token);
        // If the token is received, navigate to the '/book' route
        if (token) {
          localStorage.setItem("auth_token", token);
          localStorage.setItem("userName", this.username);
          this.router.navigate(['/redirect']);
        } else {
          // Handle the case where the token is undefined or null
          console.log('No token received');
        }
      },
      error: (error: any) => {
        // Handle login error
        console.error('Login error:', error);
      }
    });
    
  }


  // login() {
  //   this.router.navigate(['/book']);
  // }
}
