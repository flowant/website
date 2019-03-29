import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';
import { JwtModule } from '@auth0/angular-jwt';

import { AuthService, getAccessToken, setAccessToken } from './auth.service';
import { BackendService } from './backend.service';
import { Config } from '../config';
import { Auth, User, Relation, replacer } from '../_models';

export let jwt = {"access_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InVzZXIwIiwic2NvcGUiOlsibWVzc2FnZTpyZWFkIiwibWVzc2FnZTp3cml0ZSJdLCJleHAiOjE1NTM4NzMyMjksImF1dGhvcml0aWVzIjpbIlUiLCJHIl0sImp0aSI6IjhjNDgyNjg1LTg0NjAtNGQ0Zi1iMjlhLTU4Yjk3ZWNhYjUxMSIsImNsaWVudF9pZCI6ImNsaWVudCJ9.Zhc3M3vZ9sVwem7DQ9kO2IJfcfPlU7e2_XbIz3LLRoiHkzcOznXwho3G5pObKKefGk_m9ICXfotP3EBRgpsofIZOWD9DnP-L-IoxILRaUCi3q2N2ce-BH96qGafE1T5DbEwwNRP53DXiPLmrVZc19xsCRhxmw76Cj9Z-vqjxQKEl5HM2DD1rryDL_z6xIzMqmj7Rzw4I3uiKG8tTadZmCXmZzHGJE116XEGUi_KK2fH1yw3mQ_OUU5KsNSoAYSW4Kvhs0sKC1j3q7YH7-DbEwc15zeJGUJMJQYA-ffblVWhceB78qn7OQvNhROUoayVv6cWRXQ7ZL3glA-vI77ZUjQ","token_type":"bearer","refresh_token":"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InVzZXIwIiwic2NvcGUiOlsibWVzc2FnZTpyZWFkIiwibWVzc2FnZTp3cml0ZSJdLCJhdGkiOiI4YzQ4MjY4NS04NDYwLTRkNGYtYjI5YS01OGI5N2VjYWI1MTEiLCJleHAiOjE1NTY0MjIwMjksImF1dGhvcml0aWVzIjpbIlUiLCJHIl0sImp0aSI6IjY4MmI4ZmJmLTU0MGItNDAzZC04N2FjLWI4NzAyZjQyZGE5MiIsImNsaWVudF9pZCI6ImNsaWVudCJ9.DKXXrEhPVmAZ---L5Zzyc1XAckls9JAKbggzzCv2YtiZK_1AqTpEBnnZ8dk9tKXrnFzN2Aabw5WXFciKDQUL349LV0GfurOvEDWi1EUFIcdC0j4A_J-C0Y4M9cqx8NcrS_--b2bCKmkh16nG0lNOm1Ool8QHMEiZCfkPtdb6E-vxjw-bH5NEsnFevlYzdkDoPONkcOO8MHCfutNXEiFXpvs9-HXnDOovJYkRmiljIAC5rm-HqA-mlDJ-kJpL4rk8FCdOyFquggnBTR9h6M55Dbb0JoPQbEyId28tzpPs8miRuAF94Ktaclskdt1cEz16sudBSLYLPJ3NCRz8-qnFHw","expires_in":43199,"scope":"message:read message:write","jti":"8c482685-8460-4d4f-b29a-58b97ecab511"};
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

  httpTestingController.expectOne(
    req => {
      return req.method.toUpperCase() === 'POST'
          && req.url.includes(Config.authUrl)
    },
    Config.authUrl
  ).flush(auth);

  httpTestingController.expectOne(
    req => {
      return req.method.toUpperCase() === 'GET'
          && req.url.includes(Config.userUrl + '?un=' + signedInUser.username)
    },
    Config.userUrl + '?un=' + signedInUser.username
  ).flush(JSON.stringify(signedInUser, replacer));

  httpTestingController.expectOne(
    req => {
      return req.method.toUpperCase() === 'GET'
          && req.urlWithParams.includes(Config.relationUrl + '/' + signedInUser.identity);
    },
    Config.relationUrl + '/' + signedInUser.identity
  ).flush(JSON.stringify(Relation.empty, replacer));

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
        AuthService,
        BackendService
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

    httpTestingController.expectOne(
      req => {
      return req.method.toUpperCase() === 'GET'
          && req.url.includes(Config.userUrl + '?un=' + signedInUser.username)
      },
      Config.userUrl + '?un=' + signedInUser.username
    ).flush(JSON.stringify(signedInUser, replacer));

    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'GET'
            && req.urlWithParams.includes(Config.relationUrl + '/' + signedInUser.identity);
      },
      Config.relationUrl + '/' + signedInUser.identity
    ).flush(JSON.stringify(Relation.empty, replacer));
  });

  it('signIn should return User', () => {
    signIn(User.random(), TestBed.get(AuthService), httpTestingController);
  });

  it('signOut should return Guest user', () => {
    signOut(TestBed.get(AuthService));
  });

});
