import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReputationComponent } from './reputation.component';
import { AppModule } from '../app.module';

describe('ReputationComponent', () => {
  let component: ReputationComponent;
  let fixture: ComponentFixture<ReputationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ],
      imports: [ AppModule ]
    })
    TestBed.overrideModule(AppModule, {
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReputationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
