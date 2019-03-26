import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { BackendService } from './backend.service';
import { User } from '../_models';
import { Config } from '../config';

describe('BackendService', () => {

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
    httpTestingController = TestBed.get(HttpTestingController);
    service = TestBed.get(BackendService);
    logger = TestBed.get(NGXLogger);
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  describe('#User', () => {

    beforeEach(() => {
      service = TestBed.get(BackendService);
    });

    it('should be created', () => {
      expect(TestBed.get(BackendService)).toBeTruthy();
      expect(TestBed.get(NGXLogger)).toBeTruthy();
    });

    it('signUpUser should return expected user', () => {
      let expectedUser: User = User.random();

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

    it('signUpUser should return 409 status', () => {
      let existUser: User = User.guest();

      service.signUpUser(existUser).toPromise().then(
        user => fail('expected to fail'),
        error => expect(error.status).toBe(409, 'expected 409: Conflict staus')
      );

      const req = httpTestingController.expectOne(Config.signupUrl);
      expect(req.request.method).toEqual('POST');

      // respond with a 404 and the error message in the body
      req.flush('user alread exist', {status: 409, statusText: 'Conflict'});
    });

    it('getModel<User> should return expected user', () => {
      let expectedUser: User = User.guest();

      service.getModel<User>(User, expectedUser.identity).toPromise().then(
        (user: User) => {
          expect(user).toEqual(expectedUser, 'should return expected user');
          expect(user.isGuest()).toBe(true);
          expect(user.isAdmin()).toBe(false);
          expect(user.isUser()).toBe(false);
        },
        fail
      );

      const req = httpTestingController.expectOne(Config.userUrl + '/' + expectedUser.identity);
      expect(req.request.method).toEqual('GET');

      req.flush(expectedUser);
    });

    it('getModel<User> should return empty', () => {
      let expectedUser: User = User.guest();

      service.getModel<User>(User, expectedUser.identity).toPromise().then(
        user => expect(user).toBe(undefined),
        fail
      );

      const req = httpTestingController.expectOne(Config.userUrl + '/' + expectedUser.identity);
      expect(req.request.method).toEqual('GET');

      // respond with a 404 and the error message in the body
      req.flush('unknown identity', {status: 404, statusText: 'Not Found'});
    });

  });

});
