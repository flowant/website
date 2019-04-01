import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentRefComponent } from './content-ref.component';

describe('ContentRefComponent', () => {
  let component: ContentRefComponent;
  let fixture: ComponentFixture<ContentRefComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentRefComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentRefComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
