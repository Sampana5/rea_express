import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanSiteComponent } from './plan-site.component';

describe('PlanSiteComponent', () => {
  let component: PlanSiteComponent;
  let fixture: ComponentFixture<PlanSiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlanSiteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlanSiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
