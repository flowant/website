import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { BackendService } from './backend.service';
import { User, Phone, CruTime } from '../_models';
import { Config } from '../config';
import { Type } from '@angular/core';

describe('BackendService', () => {

  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
  let service: BackendService;
  let logger: NGXLogger;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        LoggerModule.forRoot({
          level: NgxLoggerLevel.TRACE,
          serverLogLevel: NgxLoggerLevel.TRACE
        })
      ],
      providers: [ BackendService ]
    });

    // Inject the http, test controller, and service-under-test
    // as they will be referenced by each test.
    httpClient = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
    service = TestBed.get(BackendService);
    logger = TestBed.get(NGXLogger);
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  describe('#signUpUser', () => {
    let expectedUser: User;

    beforeEach(() => {
      service = TestBed.get(BackendService);
      expectedUser = User.random();
    });

    it('should be created', () => {
      expect(TestBed.get(BackendService)).toBeTruthy();
      expect(TestBed.get(NGXLogger)).toBeTruthy();
    });

    it('should return expected user', () => {
      service.signUpUser(expectedUser).toPromise().then(
        (user: User) => {
          expect(user).toEqual(expectedUser, 'should return expected user');
        },
        fail
      );

      const req = httpTestingController.expectOne(Config.signupUrl);
      expect(req.request.method).toEqual('POST');

      req.flush(expectedUser);
    });

  });
  //   it('should be OK returning no heroes', () => {
  //     heroService.getHeroes().subscribe(
  //       heroes => expect(heroes.length).toEqual(0, 'should have empty heroes array'),
  //       fail
  //     );

  //     const req = httpTestingController.expectOne(heroService.heroesUrl);
  //     req.flush([]); // Respond with no heroes
  //   });

  //   it('should turn 404 into a user-friendly error', () => {
  //     const msg = 'Deliberate 404';
  //     heroService.getHeroes().subscribe(
  //       heroes => fail('expected to fail'),
  //       error => expect(error.message).toContain(msg)
  //     );

  //     const req = httpTestingController.expectOne(heroService.heroesUrl);

  //     // respond with a 404 and the error message in the body
  //     req.flush(msg, {status: 404, statusText: 'Not Found'});
  //   });

  //   it('should return expected heroes (called multiple times)', () => {
  //     heroService.getHeroes().subscribe();
  //     heroService.getHeroes().subscribe();
  //     heroService.getHeroes().subscribe(
  //       heroes => expect(heroes).toEqual(expectedHeroes, 'should return expected heroes'),
  //       fail
  //     );

  //     const requests = httpTestingController.match(heroService.heroesUrl);
  //     expect(requests.length).toEqual(3, 'calls to getHeroes()');

  //     // Respond to each request with different mock hero results
  //     requests[0].flush([]);
  //     requests[1].flush([{id: 1, name: 'bob'}]);
  //     requests[2].flush(expectedHeroes);
  //   });
  // });

  // it('want to use http client in testcase', () => {
  //   let logger: NGXLogger = TestBed.get(NGXLogger);
  //   let http: HttpClient = TestBed.get(HttpClient);
  //   let service: BackendService = new BackendService(http, logger);

  //   // is there any way to use real XHRs in testcase?
  //   http.get("https://www.google.com").toPromise().then(
  //     resp => {
  //       logger.log("resp:", resp);
  //     },
  //     error => {
  //       logger.log("error:", error);
  //     }
  //   );
  // });

});
