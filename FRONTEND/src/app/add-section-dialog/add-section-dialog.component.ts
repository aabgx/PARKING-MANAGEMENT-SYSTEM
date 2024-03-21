// src/app/components/add-section-dialog/add-section-dialog.component.ts

import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { GridService, SectionDTO } from '../service/grid.service';

@Component({
  selector: 'app-add-section-dialog',
  templateUrl: './add-section-dialog.component.html',
  styleUrls: ['./add-section-dialog.component.css']
})
export class AddSectionDialogComponent {
  section: SectionDTO = { name: '', height: 0, width: 0 };

  constructor(
    public dialogRef: MatDialogRef<AddSectionDialogComponent>,
    private gridService: GridService
  ) {}

  saveSection(): void {
    this.gridService.saveSection(this.section).subscribe({
      next: (section) => {
        console.log('Section saved:', section);
        this.dialogRef.close();
      },
      error: (error) => console.error('Error saving section:', error)
    });

    
  }
  onNoClick(): void {
    this.dialogRef.close();
  }
}
