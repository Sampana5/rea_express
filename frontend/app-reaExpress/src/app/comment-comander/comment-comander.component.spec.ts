import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentComanderComponent } from './comment-comander.component';

describe('CommentComanderComponent', () => {
  let component: CommentComanderComponent;
  let fixture: ComponentFixture<CommentComanderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommentComanderComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommentComanderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
