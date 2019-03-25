import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { BackendService } from './backend.service';

describe('BackendService', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        LoggerModule.forRoot({
          level: NgxLoggerLevel.TRACE,
          serverLogLevel: NgxLoggerLevel.TRACE
        })
      ],
      providers: []
    });
  });

  afterEach(() => {
  });

  it('should be created', () => {
    expect(TestBed.get(BackendService)).toBeTruthy();
    expect(TestBed.get(NGXLogger)).toBeTruthy();
  });

  it('want to use http client in testcase', () => {
    let logger: NGXLogger = TestBed.get(NGXLogger);
    let http: HttpClient = TestBed.get(HttpClient);
    let service: BackendService = new BackendService(http, logger);

    // is there any way to use real XHRs in testcase?
    http.get("https://www.google.com").toPromise().then(
      resp => {
        logger.log("resp:", resp);
      },
      error => {
        logger.log("error:", error);
      }
    );
  });

});
