import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TestItuneComponent } from './test-itune.component';

describe('TestItuneComponent', () => {
  let component: TestItuneComponent;
  let fixture: ComponentFixture<TestItuneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TestItuneComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TestItuneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
