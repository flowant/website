import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { AppModule } from '../app.module';
import { AuthService, BackendService } from '../_services';
import { Config } from '../config';
import { SearchContentComponent } from './search-content.component';

describe('SearchContentComponent', () => {
  let component: SearchContentComponent;
  let fixture: ComponentFixture<SearchContentComponent>;
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
        AuthService,
        BackendService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    httpTestingController = TestBed.get(HttpTestingController);
    logger = TestBed.get(NGXLogger);
  });

  // TODO
  // afterEach(() => {
  //   // After every test, assert that there are no more pending requests.
  //   httpTestingController.verify();
  // });

  it('should create', () => {
    expect(component).toBeTruthy();

    component.getPopularContents();

  });

});
