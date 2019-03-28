import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AppModule } from '../app.module';
import { AuthService, BackendService } from '../_services';
import { Config } from '../config';
import { NgbModalSendMessageComponent } from './ngb-modal-send-message.component';

describe('NgbModalSendMessageComponent', () => {
  let component: NgbModalSendMessageComponent;
  let fixture: ComponentFixture<NgbModalSendMessageComponent>;
  let httpTestingController: HttpTestingController;
  let logger: NGXLogger;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ],
      imports: [
        AppModule,
        HttpClientTestingModule,
        LoggerModule.forRoot({
          level: NgxLoggerLevel.TRACE,
          serverLogLevel: NgxLoggerLevel.TRACE
        })
      ],
      providers: [
        NgbActiveModal,
        AuthService,
        BackendService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NgbModalSendMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    httpTestingController = TestBed.get(HttpTestingController);
    logger = TestBed.get(NGXLogger);
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
