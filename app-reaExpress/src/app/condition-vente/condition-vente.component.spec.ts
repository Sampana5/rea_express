import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConditionVenteComponent } from './condition-vente.component';

describe('ConditionVenteComponent', () => {
  let component: ConditionVenteComponent;
  let fixture: ComponentFixture<ConditionVenteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConditionVenteComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConditionVenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
