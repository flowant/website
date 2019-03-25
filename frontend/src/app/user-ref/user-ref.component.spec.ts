import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserRefComponent } from './user-ref.component';
import { AppModule } from '../app.module';

describe('UserRefComponent', () => {
  let component: UserRefComponent;
  let fixture: ComponentFixture<UserRefComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ],
      imports: [ AppModule ]
    })
    TestBed.overrideModule(AppModule, {
    }).compileComponents();
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
