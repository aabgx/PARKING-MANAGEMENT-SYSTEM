import { Component, NgZone, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpHandler, HttpHeaders } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import { MatSidenavModule } from '@angular/material/sidenav';
import { DataService } from '../data.service';
import { GridService } from '../service/grid.service';


@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})


export class MainLayoutComponent implements OnInit {
  userName :string;
  userEmail :string;
  userRole : string;
  photoSrc: string;
  isSidenavOpened = false;

  constructor(
    private _ngZone: NgZone, 
    private router: Router,
    private http: HttpClient,
    private dataService: DataService,
    private gridService: GridService
    ) {

    this.userName=localStorage.getItem('userName')|| "Error"; 
    this.userEmail=localStorage.getItem('userEmail')|| "Error";
    this.userRole=localStorage.getItem('userType')|| "Error";
    this.photoSrc = "assets/App_Logo.png";
  
  }

ngOnInit(): void {
  
    
}


isErrorImage(data: string): boolean {
  // Try to extract Base64 content from the data URI
  const base64Content = data.split(',')[1];
  if (!base64Content) return true;

  // Decode Base64 content
  const decodedString = atob(base64Content);

  // Check if the decoded string is a JSON with an error property
  try {
    const json = JSON.parse(decodedString);
    return !!json.error;  // return true if 'error' property exists, false otherwise
  } catch (e) {
    // It's not a JSON, so it might be a valid image
    return false;
  }
}

isAdmin(): boolean {
  return this.userRole === 'ADMIN';
}

toggleSidenav() {
  this.isSidenavOpened = !this.isSidenavOpened;
}

toggleSidenavoff() {
  this.isSidenavOpened = false;
}


//  getMail() {
//     this.userService.getUserEmail();
//   }

  logout() {
    this.gridService.clearDashboard();
    localStorage.clear();
  }


}

