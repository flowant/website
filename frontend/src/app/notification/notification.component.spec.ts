import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { AppModule } from '../app.module';
import { AuthService, BackendService } from '../_services';
import { Config } from '../config';
import { NotificationComponent } from './notification.component';
import { User, Notification, replacer } from '../_models';
import { signIn, signOut } from '../_services/auth.service.spec';

describe('NotificationComponent', () => {
  let component: NotificationComponent;
  let fixture: ComponentFixture<NotificationComponent>;
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
    httpTestingController = TestBed.get(HttpTestingController);
    logger = TestBed.get(NGXLogger);

    // Notification component requires User role.
    let user = User.random();

    return signIn(user, TestBed.get(AuthService), httpTestingController).then(user => {
      fixture = TestBed.createComponent(NotificationComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      expect(component.user).toEqual(user);

      let notifications = [Notification.random(user.identity)];
      notifications.map(e => logger.trace("initial notification:", e.toString()));
      httpTestingController.expectOne(
        req => {
          return req.method.toUpperCase() === 'GET'
              && req.urlWithParams.includes(Config.notificationUrl + "?" + 'sid=' + user.identity);
        },
        Config.notificationUrl + "?" + 'sid=' + user.identity
      ).flush(JSON.stringify(notifications, replacer));
    });

  });

  afterEach(() => {
    signOut(TestBed.get(AuthService));
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('getNext should fetch', () => {
    expect(component).toBeTruthy();
    component.isPreview = false;
    fixture.detectChanges();

    let subscriberId = component.user.identity;
    let notifications = [Notification.random(subscriberId)];

    let cntNoti = component.notifications.length;

    component.getNext().then(_ => {
      fixture.detectChanges();
      expect(component.notifications.length).toBe(cntNoti + notifications.length);
      notifications.map(e => expect(component.notifications).toContain(e));
    });

    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'GET'
            && req.urlWithParams.includes(Config.notificationUrl + "?" + 'sid=' + subscriberId);
      },
      Config.notificationUrl + "?" + 'sid=' + subscriberId
    ).flush(JSON.stringify(notifications, replacer));
  });

  it('onDelete should remove', () => {
    expect(component).toBeTruthy();
    component.isPreview = false;
    fixture.detectChanges();

    let subscriberId = component.user.identity;

    let notification = component.notifications[0];
    component.onDelete(0).then(_ => {
      expect(component.notifications).not.toContain(notification);
    });

    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'DELETE'
            && req.urlWithParams.includes(Config.notificationUrl + "/" + notification.idCid.toString() + "/" + subscriberId);
      },
      Config.notificationUrl + "/" + notification.idCid.toString() + "/" + subscriberId
    ).flush('');
  });

});
