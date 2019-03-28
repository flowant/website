import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { UserRefComponent } from './user-ref.component';
import { AppModule } from '../app.module';
import { AuthService, BackendService } from '../_services';
import { Config } from '../config';

describe('UserRefComponent', () => {
  let httpTestingController: HttpTestingController;
  let logger: NGXLogger;
  let component: UserRefComponent;
  let fixture: ComponentFixture<UserRefComponent>;

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
        AuthService,
        BackendService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserRefComponent);
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
