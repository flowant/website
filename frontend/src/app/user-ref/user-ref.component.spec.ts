import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { UserRefComponent } from './user-ref.component';
import { AppModule } from '../app.module';
import { AuthService, BackendService } from '../_services';
import { Config } from '../config';
import { signIn, signOut } from '../_services/auth.service.spec'
import { User, Relation, replacer } from '../_models';
import { from } from 'rxjs';
import { concatMap, tap } from 'rxjs/operators';

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

  it('signIn testcases', () => {
    let refUser = User.random();
    let user = User.random();

    expect(component).toBeTruthy();
    component.userRefId = refUser.identity;
    component.userRefName = refUser.displayName;
    fixture.detectChanges();

    signIn(user, TestBed.get(AuthService), httpTestingController).then(user => {
      logger.trace("signIn testcases, signed in");
      expect(component.user).toEqual(user);
      expect(component.canShowMenu()).toBeTruthy();
    }).then(_ => {
      let p:Promise<void> = component.postRelation(true).then(relation => {
        expect(component.hasFollowee()).toBeTruthy();
        logger.trace("follow tested");
      });

      httpTestingController.expectOne(
        req => {
          return req.method.toUpperCase() === 'POST'
              && req.urlWithParams.includes(Config.relationUrl + Config.path.follow
                + "/" + component.user.identity + "/" + component.userRefId);
        },
        Config.relationUrl + Config.path.follow + "/" + component.user.identity + "/" + component.userRefId
      ).flush(JSON.stringify(Relation.of(component.user.identity, new Set().add(component.userRefId), new Set()), replacer));

      return p;

    }).then(_ => {
      let p:Promise<void> = component.postRelation(false).then(_ => {
        expect(component.hasFollowee()).toBeFalsy();
        logger.trace("unfollow tested");
      });

      httpTestingController.expectOne(
        req => {
          return req.method.toUpperCase() === 'POST'
              && req.urlWithParams.includes(Config.relationUrl + Config.path.unfollow
                + "/" + component.user.identity + "/" + component.userRefId);
        },
        Config.relationUrl + Config.path.unfollow + "/" + component.user.identity + "/" + component.userRefId
      ).flush(JSON.stringify(Relation.of(component.user.identity, new Set(), new Set()), replacer));

      return p;

    }).then(_ => signOut(TestBed.get(AuthService)));

  });

  it('should disable menu when the user and the refUser are the same', () => {
    let user = User.random();
    let refUser = user;

    expect(component).toBeTruthy();
    component.userRefId = refUser.identity;
    component.userRefName = refUser.displayName;
    fixture.detectChanges();

    signIn(user, TestBed.get(AuthService), httpTestingController)
        .then(_ => expect(component.canShowMenu()).toBeFalsy())
        .then(_ => signOut(TestBed.get(AuthService)));
  });

  it('signOut testcases', () => {
    let refUser = User.random();

    expect(component).toBeTruthy();
    component.userRefId = refUser.identity;
    component.userRefName = refUser.displayName;
    fixture.detectChanges();

    signOut(TestBed.get(AuthService)).then(
      user => {
        expect(component.user).toEqual(user);
        expect(component.canShowMenu()).toBeFalsy();

        // Guests cannot change their relations;
        component.postRelation(true)
            .then(_ => expect(component.hasFollowee()).toBeFalsy());
      }
    );
  });

});
