import { Component, OnInit } from '@angular/core';
import { GridService, ParkingSpace, Road, SectionUpdateDTO } from '../service/grid.service';
import { CompactType, DisplayGrid, GridType, GridsterConfig, GridsterItem, GridsterItemComponentInterface } from 'angular-gridster2';
import { MatDialog } from '@angular/material/dialog';
import { BookDialogComponent } from '../book-dialog/book-dialog.component';
@Component({
  selector: 'app-park-booking',
  templateUrl: './park-booking.component.html',
  styleUrl: './park-booking.component.css'
})
export class ParkBookingComponent {
  options: GridsterConfig = {} as GridsterConfig;
  dashboard:(ParkingSpace | Road)[] = [];
  selectedDate = new Date(); // Initialize with current date
  minDate = new Date();
  currentSection: string = " ";
  Sections: SectionUpdateDTO[] =[]; // This sets the minimum date to the current date


  constructor(
    private gridSerivce: GridService,
    private dialog:MatDialog
  ){}

  async ngOnInit() {
    this.gridSerivce.clearDashboard();
    this.gridSerivce.fetchAndUpdateAllParkingSpaces();
    this.gridSerivce.fetchAndUpdateAllRoads();
    this.gridSerivce.fetchAndUpdateAllBookings();
    this.gridSerivce.updateDashboardWithBookings(this.selectedDate);
    this.gridSerivce.getSections();
    this.gridSerivce.sectionsLoaded.subscribe(loaded => {
      if (loaded) {
        this.Sections = this.gridSerivce.getSectionList();
        this.currentSection = 'Select Section';
        this.initializeGridsterOptions();
      }
    });
    this.dashboard = this.gridSerivce.getDashboard();
  }

  initializeGridsterOptions() {
    this.options = {
      gridType: GridType.Fixed,
      compactType: CompactType.None,
      margin: 0,
      outerMargin: false,
      outerMarginTop: null,
      outerMarginRight: null,
      outerMarginBottom: null,
      outerMarginLeft: null,
      mobileBreakpoint: 640,
      fixedColWidth: 50,
      fixedRowHeight: 50,
      minCols: this.gridSerivce.getCurrentSectionWidth(),
      maxCols: 200,
      minRows: this.gridSerivce.getCurrentSectionHeight(),
      maxRows: 200,
      maxItemCols: 50,
      minItemCols: 1,
      maxItemRows: 50,
      minItemRows: 1,
      maxItemArea: 5000,
      minItemArea: 1,
      defaultItemCols: 1,
      defaultItemRows: 1,
      keepFixedHeightInMobile: false,
      keepFixedWidthInMobile: false,
      scrollSensitivity: 10,
      scrollSpeed: 20,
      enableEmptyCellClick: false,
      enableEmptyCellContextMenu: false,
      enableEmptyCellDrop: false,
      enableEmptyCellDrag: false,
      enableOccupiedCellDrop: false,
      emptyCellClickCallback: undefined,
      emptyCellContextMenuCallback: undefined,
      emptyCellDropCallback: undefined,
      emptyCellDragCallback: undefined,
      ignoreMarginInRow: false,
      draggable: {
        enabled: false,
      },
      resizable: {
        enabled: false,
      },
      swap: false,
      pushItems: true,
      disablePushOnDrag: false,
      disablePushOnResize: false,
      pushDirections: {north: false, east: false, south: false, west: false},
      pushResizeItems: false,
      displayGrid: DisplayGrid.Always,
      disableWindowResize: true,
      disableWarnings: false,
      scrollToNewItems: false
    };
  }

  onSectionChange(section: SectionUpdateDTO): void {
    this.gridSerivce.changeCurrentSection(section.name);
      this.currentSection = section.name;
      this.gridSerivce.setIdCounter();
      this.gridSerivce.fetchAndUpdateAllBookings();
      this.gridSerivce.updateDashboardForCurrentSection();
      this.gridSerivce.updateDashboardWithBookings(this.selectedDate);
      this.dashboard=this.gridSerivce.getDashboard();
      this.initializeGridsterOptions();
    //TO do implement this 
  }

  isMyReservation(item: ParkingSpace | Road): boolean{
    return item.ocupiedBy === localStorage.getItem("userName");
  }

  bookParking(item: ParkingSpace | Road):void{
    if(this.isParking(item)){
    if(item.occupied && this.isMyReservation(item)){
      this.gridSerivce.cancelBooking(item.id,this.gridSerivce.formatDateString(this.selectedDate), this.selectedDate)
    }else if (this.gridSerivce.canUserBook(this.gridSerivce.formatDateString(this.selectedDate)) && !item.occupied){
      if (item.type !== 'handicap')
        this.openDialog(item)
      else if (localStorage.getItem('userType') === 'SPECIAL'&& !item.occupied)
        this.openDialog(item)
    }
  }
  }

  onDateChange() {
    console.log('New Date Selected:', this.selectedDate);
    this.gridSerivce.updateDashboardWithBookings(this.selectedDate);
  }

  openDialog(parking: ParkingSpace | Road): void {
    const dialogRef = this.dialog.open(BookDialogComponent, {
      data: {parking:parking, date:this.selectedDate}
    });

  }

  isParking(item: ParkingSpace | Road): boolean {
    return item.type == 'parking'|| item.type == 'electric' || item.type == 'handicap';
  }

  isRoad(item: ParkingSpace | Road):boolean {
    return item.type == 'road' || item.type == 'intersection';
    }
 
}

