import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SaveDoneComponent } from './save-done.component';

describe('SaveDoneComponent', () => {
  let component: SaveDoneComponent;
  let fixture: ComponentFixture<SaveDoneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SaveDoneComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SaveDoneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
