import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';
import { JwtModule } from '@auth0/angular-jwt';

import { AuthService, getAccessToken, setAccessToken } from './auth.service';
import { BackendService } from './backend.service';
import { Config } from '../config';
import { Auth, User, Relation } from '../_models';

export let jwt = {"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InVzZXIwIiwic2NvcGUiOlsibWVzc2FnZTpyZWFkIiwibWVzc2FnZTp3cml0ZSJdLCJleHAiOjE1NTM2NzI4NjMsImF1dGhvcml0aWVzIjpbIlUiLCJST0xFX1VTRVIiXSwianRpIjoiN2U0OTczNzgtNDZjOC00MzEwLWE0NTktYTA0NGVmNDQ2NzgwIiwiY2xpZW50X2lkIjoiY2xpZW50In0.i8a6jp8DL4QDSCk_TXBRVkqI0sYQxzQZhkx-1ke8uAMcZYy9c3n61D-7BUPhwBpybrcL6jE1p_RK0eJQPaCqtPpQiMTwyvMFpO01meadAjIxaxdgXY2ueuV4TncRA-K_GJBuZaGnM4P1SFpN-8NwEF4WiDy9DrSa317nouL28IZCagaSCSKRTqV3Cloq51RxmAA5e21_ecIRIU7NhkdKco2fQbo4SK_xlkTdvDJItvmm6ilTwN0hMcshj9BRjaOjsp02nWtsdTbt6sfDdBI0bpuPW-E4e62jZbitorSg6NxM6aXgPI5Jtshfu4zVb1DdHT3sQMkaPh3vobHKg6XfQw","token_type":"bearer","refresh_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InVzZXIwIiwic2NvcGUiOlsibWVzc2FnZTpyZWFkIiwibWVzc2FnZTp3cml0ZSJdLCJhdGkiOiI3ZTQ5NzM3OC00NmM4LTQzMTAtYTQ1OS1hMDQ0ZWY0NDY3ODAiLCJleHAiOjE1NTYyMjE2NjMsImF1dGhvcml0aWVzIjpbIlUiLCJST0xFX1VTRVIiXSwianRpIjoiOWVhMWQ4ZmItMTQzMS00MGQ5LTkwZDgtYjNkNjgwMzYxMTM4IiwiY2xpZW50X2lkIjoiY2xpZW50In0.NcbXURIHh6wD79saXuBQDd3WyCYP58BDaJ46U0ZmkABraX6pcLy6xSlQwiTweM-wWIGvH0JRrGWwtI6G-e8slH5eTxkB0fNUJgd1z2qwUsCfjWsvRVPY1ac7RbpMyB3Z_xlDO1OEsC1lPkMbWhDaW1IFafu0B6xMfSah4UnIIm5qRdoSEE8Oyvz45rSGqzWuXXTtzvxY3GJzcj2hdCyWVTjdr3FLfQdYHuiwcZF_tSaaU3mjrSCB4xi243kwxqwTvj1TB9IunKQn8yR-x15omHUneOP0jiVt-FSlknnH6zNXIJr3myY80a7xcTvpi-0jSjHJUHN76RrJov4xxxrN3Q","expires_in":43199,"scope":"message:read message:write","jti":"7e497378-46c8-4310-a459-a044ef446780"};
export let auth: Auth = Object.assign(new Auth(), jwt);

export function signIn(signedInUser: User, service: AuthService, httpTestingController: HttpTestingController): Promise<User> {

  let promiseUser: Promise<User> = service.signIn(signedInUser.username, 'ignore password').toPromise().then(
    user => {
      expect(user.isUser()).toBe(true, 'expected User role');
      expect(user).toEqual(signedInUser, 'expected signedInUser');
      expect(service.isSignedIn()).toBe(true, 'expected signed in');
      expect(getAccessToken()).not.toBe(null, 'expected access token');
      expect(localStorage.getItem(AuthService.AUTHENTICATION)).not.toBe(null, 'expected auth data stored');
      expect(user).toEqual(TestBed.get(BackendService).getUserValue(), 'expected users are the same');
      return user;
    },
    error => {
      fail(error);
      return error;
    }
  );

  httpTestingController.expectOne(req => {
    return req.method.toUpperCase() === 'POST'
        && req.url.includes(Config.authUrl)
  }).flush(auth);

  httpTestingController.expectOne(req => {
    return req.method.toUpperCase() === 'GET'
        && req.url.includes(Config.userUrl + '?un=' + signedInUser.username)
  }).flush(signedInUser);

  httpTestingController.expectOne(req => {
    return req.method.toUpperCase() === 'GET'
        && req.urlWithParams.includes(Config.relationUrl + '/' + signedInUser.identity);
  }).flush(Relation.empty);

  return promiseUser;
}

export function signOut(service: AuthService): Promise<User> {
  return service.signOut().toPromise().then(
    user => {
      expect(user.isGuest()).toBe(true, 'expected Guest role');
      expect(service.isSignedIn()).toBe(false, 'expected signed out');
      expect(service.isTokenExpired()).toBe(true, 'expected Token expired');
      expect(getAccessToken()).toBe(null, 'expected null access token');
      expect(localStorage.getItem(AuthService.AUTHENTICATION)).toBe(null, 'expected null auth data');
      return user;
    },
    error => {
      fail(error);
      return error;
    }
  );
}

describe('AuthService', () => {

  let httpTestingController: HttpTestingController;
  let logger: NGXLogger;
  // TODO generate

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        JwtModule.forRoot({
          config: {
            tokenGetter: getAccessToken,
            whitelistedDomains: Config.whitelistedDomains,
            blacklistedRoutes: Config.blacklistedRoutes
          }
        }),
        LoggerModule.forRoot({
          level: NgxLoggerLevel.TRACE,
          serverLogLevel: NgxLoggerLevel.TRACE
        })
      ],
      providers: [
        AuthService
      ]
    });

    httpTestingController = TestBed.get(HttpTestingController);
    logger = TestBed.get(NGXLogger);

    // AuthService has an initialization routine based on data in localStorage.
    setAccessToken();
  });

  afterEach(() => {
    // After every test, assert that there are no more pending requests.
    httpTestingController.verify();
  });

  it('should be created', () => {
    let service: AuthService = TestBed.get(AuthService);
    expect(service).toBeTruthy();
  });

  it('loading unexpired JWT token should return User', () => {

    let signedInUser = User.random();
    auth.username = signedInUser.username;
    localStorage.setItem(AuthService.AUTHENTICATION, JSON.stringify(auth));
    setAccessToken(auth.access_token);

    let service: AuthService = TestBed.get(AuthService);

    if (service.isTokenExpired()) {
      expect(service).toBeTruthy();
      return;
    }

    TestBed.get(BackendService).getUser().toPromise().then(
      user => {
        expect(user.isUser()).toBe(true, 'expected User role');
        expect(user).toEqual(signedInUser, 'expected signedInUser');
      }
    )

    expect(service.isSignedIn()).toBe(true, 'expected signed in');
    expect(getAccessToken()).not.toBe(null, 'expected access token');
    expect(localStorage.getItem(AuthService.AUTHENTICATION)).not.toBe(null, 'expected auth data stored');

    httpTestingController.expectOne(req => {
      return req.method.toUpperCase() === 'GET'
          && req.url.includes(Config.userUrl + '?un=' + signedInUser.username)
    }).flush(signedInUser);

    httpTestingController.expectOne(req => {
      return req.method.toUpperCase() === 'GET'
          && req.urlWithParams.includes(Config.relationUrl + '/' + signedInUser.identity);
    }).flush(Relation.empty);

  });

  it('signIn should return User', () => {
    signIn(User.random(), TestBed.get(AuthService), httpTestingController);
  });

  it('signOut should return Guest user', () => {
    signOut(TestBed.get(AuthService));
  });

});
