import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserRefComponent } from './user-ref.component';

describe('UserRefComponent', () => {
  let component: UserRefComponent;
  let fixture: ComponentFixture<UserRefComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserRefComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserRefComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
