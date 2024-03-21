import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { GridService, ParkingSpace,Road, SectionUpdateDTO } from '../service/grid.service';
import { CompactType, DisplayGrid, GridType, GridsterComponent, GridsterConfig, GridsterItem, GridsterItemComponentInterface } from 'angular-gridster2';
import { MatDialog } from '@angular/material/dialog';
import { AddSectionDialogComponent } from '../add-section-dialog/add-section-dialog.component';

@Component({
  selector: 'app-park-admin',
  templateUrl: './park-admin.component.html',
  styleUrl: './park-admin.component.css'
})
export class ParkAdminComponent implements OnInit, AfterViewInit{



  options: GridsterConfig = {} as GridsterConfig;
  dashboard:(ParkingSpace | Road)[] = [];
  addParkingMode = false;
  addRoadMode= false;
  switchTypeMode= false;
  currentSection: string = " ";
  Sections: SectionUpdateDTO[] =[];



  constructor(
    private gridSerivce: GridService,
    private dialog: MatDialog
  ){}

  async ngOnInit() {
    this.gridSerivce.clearDashboard();
    this.gridSerivce.fetchAndUpdateAllParkingSpaces();
    this.gridSerivce.fetchAndUpdateAllRoads();
    this.gridSerivce.fetchAndUpdateAllBookings();
    this.gridSerivce.getSections();
    this.gridSerivce.sectionsLoaded.subscribe(loaded => {
      if (loaded) {
        this.Sections = this.gridSerivce.getSectionList();
        this.currentSection = 'Selct Section';
        this.initializeGridsterOptions();
      }
    });
    this.dashboard = this.gridSerivce.getDashboard();
  }

  ngAfterViewInit() {
    this.currentSection = this.gridSerivce.getCurrentSection();
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
        enableEmptyCellClick: true,
        enableEmptyCellContextMenu: false,
        enableEmptyCellDrop: false,
        enableEmptyCellDrag: false,
        enableOccupiedCellDrop: false,
        emptyCellClickCallback: this.onEmptyCellClick.bind(this),
        emptyCellContextMenuCallback: undefined,
        emptyCellDropCallback: undefined,
        emptyCellDragCallback: undefined,
        ignoreMarginInRow: false,
        draggable: {
          enabled: true,
        },
        resizable: {
          enabled: true,
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

  onRotateClick(griditem:ParkingSpace | Road): void{
    this.gridSerivce.rotate(griditem);
    this.dashboard = this.gridSerivce.getDashboard();
    console.log("rotate clicked");
  }

 

  toggleaAddParking(): void{
    this.addParkingMode = !this.addParkingMode;
    this.addRoadMode = false;
    this.switchTypeMode = false;
  }

  toggleaAddRoad(): void {
    this.addRoadMode = !this.addRoadMode;
    this.addParkingMode = false;
    this.switchTypeMode = false;
    }

  toggleType():void {
    this.switchTypeMode = !this.switchTypeMode;
    this.addRoadMode = false;
    this.addParkingMode = false;
      
    }

    onSectionChange(section: SectionUpdateDTO): void {
      this.gridSerivce.changeCurrentSection(section.name);
      this.currentSection = section.name;
      this.gridSerivce.setIdCounter();
      this.gridSerivce.updateDashboardForCurrentSection();
      this.dashboard=this.gridSerivce.getDashboard();
      this.initializeGridsterOptions();
      //TO do implement this 
    }
  

  onEmptyCellClick(event: MouseEvent, item: GridsterItem): void {
    console.log('Empty cell clicked', item);
    if (this.addParkingMode) { // assuming you have a mode to toggle adding parking
      this.addParking(item.x, item.y);
    }else if(this.addRoadMode){
      this.addRoad(item.x, item.y)
    }
  }

  addParking(x:number, y:number): void{
    this.gridSerivce.addParking(x,y);
  }

  addRoad(x:number, y:number):void{
    this.gridSerivce.addRoad(x,y);
  }

  saveChanges(): void{
    this.addParkingMode = false;
    this.addRoadMode = false;
    this.switchTypeMode = false;
    this.gridSerivce.saveAllParkingAndRoads();
  }

  deleteGridItem(parking: { id: number; }) {
    this.gridSerivce.deleteParking(parking.id);
    this.dashboard = this.gridSerivce.getDashboard();
  }

  isParking(item: ParkingSpace | Road): boolean {
    return item.type == 'parking'|| item.type == 'electric' || item.type == 'handicap';
  }

  isRoad(item: ParkingSpace | Road):boolean {
    return item.type == 'road' || item.type == 'intersection';
    }

    toggleParkingType(griditem: any) {
      if (griditem.type === 'parking') {
        griditem.type = 'electric';
      } else if (griditem.type === 'electric') {
        griditem.type = 'handicap';
      } else if (griditem.type === 'handicap') {
        griditem.type = 'parking';
      } else if (griditem.type === 'road'){
        griditem.type = 'intersection';
      }else if (griditem.type === 'intersection'){
        griditem.type = 'road';
      }
    }

    deleteCurrentSection() {
      const currentSectionObj = this.Sections.find(section => section.name === this.currentSection);
      if (currentSectionObj && currentSectionObj.id != null) {
        const confirmDeletion = confirm(`Are you sure you want to delete the section "${this.currentSection}"?`);
        if (confirmDeletion) {
          this.gridSerivce.deleteSection(currentSectionObj.id).subscribe({
            next: () => console.log('Section deleted successfully'),
            error: (error) => console.error('Error deleting section:', error)
          });
        }
      } else {
        alert('No section is currently selected or section ID is missing.');
      }
    }

    openAddSectionDialog(): void {
      const dialogRef = this.dialog.open(AddSectionDialogComponent, {
        width: '250px'
      });
  
      dialogRef.afterClosed().subscribe(result => {
        console.log('The dialog was closed');
        // Handle dialog result if needed
      });
    }




}
