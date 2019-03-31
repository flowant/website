import { async, ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { AppModule } from '../app.module';
import { AuthService, BackendService } from '../_services';
import { Config } from '../config';
import { ContentViewerComponent } from './content-viewer.component';
import { Content, replacer, WebSite } from '../_models';

describe('ContentViewerComponent', () => {
  let component: ContentViewerComponent;
  let fixture: ComponentFixture<ContentViewerComponent>;
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
    fixture = TestBed.createComponent(ContentViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    logger = TestBed.get(NGXLogger);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // TODO test with mock ActivatedRoute

});
