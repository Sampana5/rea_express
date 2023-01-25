import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanAccesComponent } from './plan-acces.component';

describe('PlanAccesComponent', () => {
  let component: PlanAccesComponent;
  let fixture: ComponentFixture<PlanAccesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlanAccesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlanAccesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
