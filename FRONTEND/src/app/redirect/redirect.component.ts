import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { User } from '../users-dashboard/user.model';
import { UserDataDTO, UsersService } from '../service/users.service';
import { GridService } from '../service/grid.service';

@Component({
  selector: 'app-redirect',
  templateUrl: './redirect.component.html',
  styleUrl: './redirect.component.css'
})
export class RedirectComponent implements OnInit{

  constructor(private gridService: GridService, private userService: UsersService, private router: Router){}
  

  ngOnInit(): void {
    const username=localStorage.getItem('userName');
    if(username != null){
      this.userService.getUserData(username).subscribe(
        (userData: UserDataDTO) => {
          localStorage.setItem('userEmail', userData.email);
          localStorage.setItem('userType', userData.userType);
          localStorage.setItem('phone', userData.phone);
          this.router.navigate(['/book']);
          this.gridService.getSections();
          this.gridService.fetchAndUpdateAllParkingSpaces();
          this.gridService.fetchAndUpdateAllRoads();
          this.gridService.fetchAndUpdateAllBookings();
          
        },
        (error) => {
          console.error('Error fetching user data:', error);
        }
      );
    }
    
  }

}

