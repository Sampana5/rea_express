import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VoirPanierComponent } from './voir-panier.component';

describe('VoirPanierComponent', () => {
  let component: VoirPanierComponent;
  let fixture: ComponentFixture<VoirPanierComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VoirPanierComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VoirPanierComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
