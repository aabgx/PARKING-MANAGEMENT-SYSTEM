import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { waitForAsync } from '@angular/core/testing';
import { GridsterItem } from 'angular-gridster2';
import { Observable, forkJoin, map, tap } from 'rxjs';
import { SaveDoneComponent } from '../save-done/save-done.component';
import { MatDialog } from '@angular/material/dialog';

export interface BookingDTO {
  userName: string;
  parkingSpaceId: number;
  date: string; // Format: "yyyy-MM-dd"
}

export interface ParkingSpaceGridDTO {
  id: number;
  type: string;
  x: number;
  y: number;
  width: number;
  height: number;
  orientation: string;
  section: string;
}

export interface RoadDTO {
  id: number;
  cols: number;
  rows: number;
  x: number;
  y: number;
  type: string;
  section: string;
  orientation: string;
}

export interface ParkingSpace extends GridsterItem {
  section: string;
  orientation:string;
  id: number;
  occupied: boolean;
  ocupiedBy:string | null;
  type: string;
  date: string | null;
}

export interface Road extends GridsterItem {
  section: string;
  orientation:string;
  id: number;
  type: string;
}

export interface Booking {
  parkingSpaceId: number;
  userName: string | null;
  date: string;
  }

  export interface SectionDTO {
    name: string;
    height: number;
    width: number;
  }

  export interface SectionUpdateDTO {
    id: number;
    name: string;
    height: number;
    width: number;
  }

@Injectable({
  providedIn: 'root'
})
export class GridService {
  private apiUrl = 'http://localhost:8081/api';
  private IdCounter = 1;
  private currentSection: string = 'Select Section';
  sections: SectionUpdateDTO[] = [];
  private allRoads: Road[] =[] ;
  private allParking: ParkingSpace[] =[];
  sectionsLoaded = new EventEmitter<boolean>();
  private dashboard: (ParkingSpace | Road)[] = [];

  constructor(private http: HttpClient, private dialog: MatDialog) {}

  private bookings: Booking[ ] = [];
  

  getDashboard(): (ParkingSpace | Road)[] {
    return this.dashboard;
  }

  clearDashboard(): void {
    this.dashboard = [];

  }

  updateDashboardWithBookings(date: Date): void {
    const givenDate=this.formatDateString(date);
    this.dashboard.forEach(item => {
    if (this.isParking(item)) {
    item.occupied = false;
    item.ocupiedBy = null;
    }
    });

    this.bookings.filter(booking => booking.date === givenDate).forEach(booking => {
      const parkingSpace = this.dashboard.find(item => item.id === booking.parkingSpaceId && this.isParking(item)) as ParkingSpace;
      if (parkingSpace) {
        parkingSpace.occupied = true;
        parkingSpace.ocupiedBy = booking.userName;
      }
    });
  }

  updateDashboardForCurrentSection(): void {
    // Clear the dashboard
    this.dashboard = [];

    // Add roads to the dashboard that belong to the current section
    this.allRoads.forEach(road => {
      if (road.section === this.currentSection) {
        this.dashboard.push(road);
      }
    });

    // Add parking spaces to the dashboard that belong to the current section
    this.allParking.forEach(parking => {
      if (parking.section === this.currentSection) {
        this.dashboard.push(parking);
      }
    });
  }


  addParking(x:number,y:number):void {
    const newDesk: ParkingSpace = {
      cols: 2,
      rows: 3,
      y: y,
      x: x,
      id: this.IdCounter,
      occupied: false,
      date: null,
      type: 'parking',
      orientation:'NORTH',
      ocupiedBy:null,
      section: this.currentSection
    };
    this.IdCounter++;
    this.dashboard.push(newDesk);
  }

  addRoad(x:number,y:number):void {
    const newRoad: Road = {
      cols: 3,
      rows: 3,
      y: y,
      x: x,
      id: this.IdCounter,
      occupied: false,
      type: 'road',
      orientation:'NORTH',
      section: this.currentSection
    };
    this.IdCounter++;
    this.dashboard.push(newRoad);
  }

  rotate(griditem: ParkingSpace | Road){
    let aux = griditem.cols;
    griditem.cols = griditem.rows;
    griditem.rows = aux;
    switch(griditem.orientation){
      case 'NORTH':{
        griditem.orientation = 'WEST';
        break;
      }
      case 'WEST':{
        griditem.orientation = 'SOUTH';
        break;
      }
      case 'SOUTH':{
        griditem.orientation = 'EAST';
        break;
      }
      case 'EAST':{
        griditem.orientation = 'NORTH';
        break;
      }
      default:
        console.log("Rotarion Error!");
        break;
    }

    this.dashboard = [...this.dashboard];
  }

  deleteParking(id: number) {
    const index = this.dashboard.findIndex(ParkingSpace => ParkingSpace.id === id);
    if (index !== -1) {
      this.dashboard.splice(index, 1);
    }
  }

  isParking(item: ParkingSpace | Road): boolean {
    return item.type == 'parking'|| item.type == 'electric' || item.type == 'handicap';
  }

  isRoad(item: ParkingSpace | Road):boolean {
    return item.type == 'road' || item.type == 'intersection';
    }

    formatDateString(date: Date): string {
      const year = date.getFullYear();
      const month = date.toLocaleString('default', { month: 'short' }); // e.g., 'Jan'
      const day = date.getDate();
    
      return `${day} ${month} ${year}`; // e.g., 'Jan 19, 2024'
    }

    printDashboardAndBookings(): void {
      console.log('Dashboard:', this.dashboard);
      console.log('Bookings:', this.bookings);
    }

    

    registerBooking(booking: { parkingSpaceId: number, userName: string| null, date: string }): void {
      this.bookings.push(booking);
    }

  cancelBooking(parkingSpaceId: number, date: string, selectedDate : Date): void {
      const index= this.bookings.findIndex(
    booking => booking.parkingSpaceId === parkingSpaceId && booking.date === date
    );
    if (index !== -1) {
    this.bookings.splice(index, 1);
    const parkingSpace = this.dashboard.find(
      item => this.isParking(item) && item.id === parkingSpaceId
    ) as ParkingSpace;
    if (parkingSpace) {

      parkingSpace.occupied = false;
      parkingSpace.ocupiedBy = null;
      parkingSpace.date = null;

      let bookingDTO: BookingDTO = {
        userName: localStorage.getItem('userName') ?? 'error' ,
        parkingSpaceId: parkingSpaceId,
        date: this.formatDateStringv2(selectedDate)
      };

      this.deleteBooking(bookingDTO).subscribe({
        next: () => console.log('Booking deleted successfully'),
        error: error => console.error('Error deleting booking:', error)
      });
      }}
  }

  deleteBooking(bookingDTO: BookingDTO): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post(`${this.apiUrl}/booking/delete`, bookingDTO, { headers });
  }

  formatDateStringv2(date: Date): string {
    const year = date.getFullYear();
    const month = date.getMonth() + 1; // JavaScript months are 0-indexed
    const day = date.getDate();
  
    // Format as "yyyy-MM-dd"
    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
  }

  canUserBook( date: string): boolean {
    if (localStorage.getItem('userType')==='ADMIN')
      return true;
    return !this.bookings.some(booking => booking.userName === localStorage.getItem('userName') && booking.date === date);
    }

    getSections(): void {
    this.getAllSections().subscribe({
      next: (sections) => {
        this.sections = sections;
        if (sections.length > 0 && !this.currentSection) {
          this.currentSection = sections[0].name;
        }
        this.sectionsLoaded.emit(true); // Emit event after sections are loaded
      },
      error: (error) => {
        console.error('Error fetching sections:', error);
        this.sectionsLoaded.emit(false);
      }
    });
  }

  updateAllParkingAndRoads(): void {
    this.dashboard.forEach(item => {
      if (this.isParking(item)) {
        const parkingIndex = this.allParking.findIndex(p => p.id === item.id);
        if (parkingIndex !== -1) {
          // Replace existing parking space
          this.allParking[parkingIndex] = item as ParkingSpace;
        } else {
          // Add new parking space
          this.allParking.push(item as ParkingSpace);
        }
      } else if (this.isRoad(item)) {
        const roadIndex = this.allRoads.findIndex(r => r.id === item.id);
        if (roadIndex !== -1) {
          // Replace existing road
          this.allRoads[roadIndex] = item as Road;
        } else {
          // Add new road
          this.allRoads.push(item as Road);
        }
      }
    });
    console.log(this.allParking);
    console.log(this.allRoads);
  }

  setIdCounter(): void {
    let maxRoadId = 0;
    let maxParkingId = 0;

    // Find the maximum ID in allRoads
    if (this.allRoads.length > 0) {
      maxRoadId = Math.max(...this.allRoads.map(road => road.id));
    }

    // Find the maximum ID in allParking
    if (this.allParking.length > 0) {
      maxParkingId = Math.max(...this.allParking.map(parking => parking.id));
    }

    // Determine the maximum ID between roads and parking spaces
    const maxId = Math.max(maxRoadId, maxParkingId);

    // Set IdCounter to maxId + 1, or to 1 if both lists are empty
    this.IdCounter = maxId > 0 ? maxId + 1 : 1;
  }


  saveAllParkingAndRoads(): void {
    this.updateAllParkingAndRoads();

    this.overrideExistingParkingSpaces().subscribe({
      next: (response) => {
        this.dialog.open(SaveDoneComponent);
      },
      error: (error) => {
        console.error('Error saving parking spaces', error);
      }
    });

    // Create observables for saving roads and parking spaces
    this.saveAllRoads().subscribe({
      next: (response) => {
        console.log('Roads saved successfully', response);
      },
      error: (error) => {
        console.error('Error saving roads', error);
      }
    });
    
    

  
  }

    resetSectionsLoaded() {
      this.sectionsLoaded.emit(false);
    }

    getCurrentSectionHeight(): number {
      const currentSection = this.sections.find(section => section.name === this.currentSection);
      return currentSection ? currentSection.height : 0;
    }
  
    getCurrentSectionWidth(): number {
      const currentSection = this.sections.find(section => section.name === this.currentSection);
      return currentSection ? currentSection.width : 0;
    }

    getCurrentSection(): string {
      return this.currentSection;
    }

    changeCurrentSection(newSectionName: string): void {
      this.currentSection = newSectionName;
    }

    getSectionList(): SectionUpdateDTO[]{
      return this.sections;
    }

    saveSection(sectionDTO: SectionDTO): Observable<SectionDTO> {
      return this.http.post<SectionDTO>(`${this.apiUrl}/section/save`, sectionDTO, { headers: this.getHeaders() }).pipe(
        tap({
          next: (savedSection) => {
            this.getSections(); // Refetch sections and update list
          },
          error: (error) => console.error('Error saving section:', error)
        })
      );
      // const headers = this.getHeaders();
      // return this.http.post<SectionDTO>(`${this.apiUrl}/section/save`, sectionDTO, { headers });
    }


    getAllSections(): Observable<SectionUpdateDTO[]> {
      const headers = this.getHeaders();
      return this.http.get<SectionUpdateDTO[]>(`${this.apiUrl}/section/get`, { headers });
    }

    deleteSection(sectionId: number): Observable<any> {
      const headers = this.getHeaders();
      return this.http.post(`${this.apiUrl}/section/delete/${sectionId}`, null, { headers }).pipe(
        tap({
          next: () => this.getSections(), // Refetch sections after deletion
          error: (error) => console.error('Error deleting section:', error)
        })
      );
    }
    private getHeaders(): HttpHeaders {
      let headers = new HttpHeaders();
      const token = localStorage.getItem('auth_token');
      if (token) {
        headers = headers.set('Authorization', `Bearer ${token}`);
      }
      return headers;
    }

    saveAllRoads(): Observable<any> {
      const roadDTOs = this.allRoads.map(road => ({
        id: road.id,
        cols: road.cols,
        rows: road.rows,
        x: road.x,
        y: road.y,
        section: road.section,
        type: road.type,
        orientation: road.orientation
      }));
      const body = { roads: roadDTOs };
      const headers = this.getHeaders();
    
      return this.http.post(`${this.apiUrl}/road/saveAllRoads`, body, { headers });
    }
    
    overrideExistingParkingSpaces(): Observable<any> {
      const parkingSpaceDTOs = this.allParking.map(parking => ({
        id: parking.id,
        x: parking.x,
        y: parking.y,
        width: parking.cols,
        height: parking.rows,
        section: parking.section,
        type: parking.type,
        orientation: parking.orientation
      }));
      const body = { parkingSpaces: parkingSpaceDTOs };
      const headers = this.getHeaders();
    
      return this.http.post(`${this.apiUrl}/parkingSpace/overrideParkingSpaces`, body, { headers });
    }

    getAllParkingSpaces(): Observable<ParkingSpace[]> {
      const headers = this.getHeaders();
      return this.http.get<ParkingSpaceGridDTO[]>(`${this.apiUrl}/parkingSpace/get`, { headers })
        .pipe(
          map((response: ParkingSpaceGridDTO[]) => {
            // Convert response to ParkingSpace array and update allParking
            const parkingSpaces: ParkingSpace[] = response.map(p => ({
              id: p.id,
              cols: p.width,
              rows: p.height,
              x: p.x,
              y: p.y,
              section: p.section,
              orientation: p.orientation,
              type: p.type.toLowerCase(),
              occupied: false, // Default value
              ocupiedBy: null,  // Default value
              date: null       // Default value
            }));
            this.allParking = parkingSpaces;
            return parkingSpaces;
          })
        );
    }

    fetchAndUpdateAllParkingSpaces(): void {
      this.getAllParkingSpaces().subscribe({
        next: parkingSpaces => {
          this.allParking = parkingSpaces;
          console.log('All parking spaces updated:', this.allParking);
        },
        error: error => console.error('Error fetching parking spaces:', error)
      });
    }

    getAllRoads(): Observable<Road[]> {
      const headers = this.getHeaders();
      return this.http.get<RoadDTO[]>(`${this.apiUrl}/road/get`, { headers })
        .pipe(
          map((response: RoadDTO[]) => {
            // Convert response to Road array and update allRoads
            const roads: Road[] = response.map(r => ({
              id: r.id,
              cols: r.cols,
              rows: r.rows,
              x: r.x,
              y: r.y,
              section: r.section,
              orientation: r.orientation,
              type: r.type.toLowerCase()
            }));
            this.allRoads = roads;
            return roads;
          })
        );
    }
  
    fetchAndUpdateAllRoads(): void {
      this.getAllRoads().subscribe({
        next: roads => {
          this.allRoads = roads;
          console.log('All roads updated:', this.allRoads);
        },
        error: error => console.error('Error fetching roads:', error)
      });
    }

    getAllBookings(): Observable<Booking[]> {
      const headers = this.getHeaders();
      return this.http.get<BookingDTO[]>(`${this.apiUrl}/booking/get`, { headers })
        .pipe(
          map((response: BookingDTO[]) => {
            // Convert response to Booking array and update bookings
            const bookings: Booking[] = response.map(b => ({
              parkingSpaceId: b.parkingSpaceId,
              userName: b.userName,
              date: this.convertDateToDisplayFormat(b.date) // Convert to "dd MMM yyyy" format
            }));
            this.bookings = bookings;
            return bookings;
          })
        );
    }
  
    fetchAndUpdateAllBookings(): void {
      this.getAllBookings().subscribe({
        next: bookings => {
          this.bookings = bookings;
          console.log('All bookings updated:', this.bookings);
        },
        error: error => console.error('Error fetching bookings:', error)
      });
    }
  
    private convertDateToDisplayFormat(isoDate: string): string {
      const date = new Date(isoDate);
      return `${date.getDate()} ${date.toLocaleString('default', { month: 'short' })} ${date.getFullYear()}`;
    }

    saveBooking(booking: { parkingSpaceId: number, userName: string, date: string }): Observable<number> {
      const headers = this.getHeaders();
      const body = {
        userName: booking.userName,
        parkingSpaceId: booking.parkingSpaceId,
        date: booking.date
      };
      return this.http.post<number>(`${this.apiUrl}/booking/save`, body, { headers });
    }
    
  
}
