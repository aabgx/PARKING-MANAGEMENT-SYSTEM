import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ParkingSpace, Road } from '../service/grid.service';
import { GridService } from '../service/grid.service';

@Component({
  selector: 'app-book-dialog',
  templateUrl: './book-dialog.component.html',
  styleUrls: ['./book-dialog.component.css']
})
export class BookDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<BookDialogComponent>,
    public gridService: GridService,
    @Inject(MAT_DIALOG_DATA) public data: { parking: ParkingSpace, date: Date }
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  bookParking(): void {
      this.data.parking.occupied = true;
        this.data.parking.ocupiedBy = localStorage.getItem('userName');
        this.data.parking.date = this.formatDateString(this.data.date);
        this.gridService.registerBooking({
        parkingSpaceId: this.data.parking.id,
        userName: this.data.parking.ocupiedBy,
        date: this.data.parking.date
        });

    this.gridService.saveBooking({
      parkingSpaceId: this.data.parking.id,
      userName: localStorage.getItem('userName') || '',
      date: this.formatDateStringv2(this.data.date)
    }).subscribe({
      next: (bookingId) => {
        console.log(`Booking created successfully with ID: ${bookingId}`);
        
      },
      error: (error) => console.error('Error saving booking:', error)
    });
    this.dialogRef.close();
  }

  formatDateString(date: Date): string {
    const year = date.getFullYear();
    const month = date.toLocaleString('default', { month: 'short' }); // e.g., 'Jan'
    const day = date.getDate();
  
    return `${day} ${month} ${year}`; // e.g., 'Jan 19, 2024'
  }

  formatDateStringv2(date: Date): string {
    const year = date.getFullYear();
    const month = date.getMonth() + 1; // JavaScript months are 0-indexed
    const day = date.getDate();
  
    // Format as "yyyy-MM-dd"
    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
  }
}