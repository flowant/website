import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NgbModalSendMessageComponent } from './ngb-modal-send-message.component';

describe('NgbModalSendMessageComponent', () => {
  let component: NgbModalSendMessageComponent;
  let fixture: ComponentFixture<NgbModalSendMessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NgbModalSendMessageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NgbModalSendMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
