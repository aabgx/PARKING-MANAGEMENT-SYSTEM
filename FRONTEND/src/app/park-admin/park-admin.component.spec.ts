import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParkAdminComponent } from './park-admin.component';

describe('ParkAdminComponent', () => {
  let component: ParkAdminComponent;
  let fixture: ComponentFixture<ParkAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ParkAdminComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ParkAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
