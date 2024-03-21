import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParkBookingComponent } from './park-booking.component';

describe('ParkBookingComponent', () => {
  let component: ParkBookingComponent;
  let fixture: ComponentFixture<ParkBookingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ParkBookingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ParkBookingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
