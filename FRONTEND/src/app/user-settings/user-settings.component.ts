import {Component, OnInit} from '@angular/core';
import {User} from './user.model';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import { UserModifyDTO, UsersService } from '../service/users.service';

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrl: './user-settings.component.css'
})
export class UserSettingsComponent implements OnInit{
  newPassword:string='';
  confirmPassword:string='';

  showSuccessAnimationt = false;
  showErrorAnimationt = false;
  user = {
    username: localStorage.getItem('userName') ?? 'defaultUsername',
    phone: localStorage.getItem('phone') ?? 'defaultPhone',
    email: localStorage.getItem('userEmail') ?? 'defaultEmail',
    userType: localStorage.getItem('userType') ?? 'defaultUserType',
  };
  constructor(private usersService: UsersService) {}
  ngOnInit(): void {
  }

  submitForm(): void {
    const userDTO: UserModifyDTO = {
      username: this.user.username,
      phone: this.user.phone,
      email: this.user.email,
      newPassword: this.newPassword,
      confirmNewPassword: this.confirmPassword
    };

    this.usersService.modifyUserOwnData(userDTO).subscribe(
      response => {
        localStorage.setItem('userName', this.user.username);
        localStorage.setItem('phone', this.user.phone);
        localStorage.setItem('userEmail', this.user.email);
        localStorage.setItem('auth_token', response);
        console.log('User data modified successfully:', response);
        this.showSuccessAnimation(); // Implement this method for success animation
      },
      error => {
        // Handle error
        console.error('Error modifying user data:', error);
        this.showErrorAnimation(); // Implement this method for error animation
      }
    );

 }

 showSuccessAnimation(): void {
  this.showSuccessAnimationt = true;
  setTimeout(() => this.showSuccessAnimationt = false, 3000); // Hide after 3 seconds
}

showErrorAnimation(): void {
  this.showErrorAnimationt = true;
  setTimeout(() => this.showErrorAnimationt = false, 3000); // Hide after 3 seconds
}

}
