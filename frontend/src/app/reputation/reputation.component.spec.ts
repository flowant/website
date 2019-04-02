import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { AppModule } from '../app.module';
import { AuthService, BackendService } from '../_services';
import { Config } from '../config';
import { ReputationComponent } from './reputation.component';
import { User, Reputation, Review, IdCid, Notification, replacer, Category } from '../_models';
import { signOut, signIn } from '../_services/auth.service.spec';

describe('ReputationComponent', () => {
  let component: ReputationComponent;
  let fixture: ComponentFixture<ReputationComponent>;
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
    fixture = TestBed.createComponent(ReputationComponent);
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

  it('Rated testcases', () => {
    expect(component).toBeTruthy();

    let user: User = User.random();
    let author: User = User.random();

    component.typeName = "Content";
    component.authorId = author.identity;
    component.idCid = IdCid.random();
    component.reputation = new Reputation();
    fixture.detectChanges();

    expect(component.hasRated()).toBeTruthy();
    expect(component.reputation.avrRated()).toEqual('0');
    component.typeName = "Review";
    fixture.detectChanges();

    expect(component.hasRated()).toBeFalsy();
    component.typeName = "Reply";
    fixture.detectChanges();
    expect(component.hasRated()).toBeFalsy();
  });

  it('User authority testcases', () => {
    expect(component).toBeTruthy();

    let user: User = User.random();
    let author: User = User.random();
    let review = Review.random(author);

    component.typeName = "Review";
    component.authorId = review.authorId;
    component.idCid = review.idCid;
    component.reputation = review.reputation;
    fixture.detectChanges();

    signIn(user, TestBed.get(AuthService), httpTestingController).then(user => {
      expect(component.user).toEqual(user);
      expect(component.canRepute()).toBeTruthy();
      expect(component.isLiked()).toBeFalsy();

    }).then(() => {

      let p: Promise<void> = component.onRepute('liked').then(liked => {
        expect(component.isLiked()).toBeTruthy();
        expect(component.reputation.liked).toBe(1);
      });

      user.likes.add(component.idCid.identity);

      httpTestingController.expectOne(
        req => {
          return req.method.toUpperCase() === 'POST'
              && req.url.includes(Config.userUrl)
        },
        Config.userUrl
      ).flush(JSON.stringify(user, replacer));

      let expectedRpt: Reputation = new Reputation();
      expectedRpt.liked = 1;

      httpTestingController.expectOne(
        req => {
        return req.method.toUpperCase() === 'POST'
            && req.url.includes(Config.reviewRptUrl)
        },
        Config.reviewRptUrl
      ).flush(JSON.stringify(expectedRpt, replacer));

      let notification = Notification.of(user.identity, user.displayName, Category.Like,
          component.authorId, component.idCid.identity, component.idCid.containerId, component.typeName);

      httpTestingController.expectOne(
        req => {
          return req.method.toUpperCase() === 'POST'
              && req.url.includes(Config.notificationUrl)
        },
        Config.notificationUrl
      ).flush(JSON.stringify(notification, replacer));

      return p;

    }).then(() => {

      let p: Promise<void> = component.onRepute('liked').then(unliked => {
        expect(component.isLiked()).toBeFalsy()
        expect(component.reputation.liked).toBe(0);
      });

      user.likes.delete(component.idCid.identity);
      httpTestingController.expectOne(
        req => {
          return req.method.toUpperCase() === 'POST'
              && req.url.includes(Config.userUrl)
        },
        Config.userUrl
      ).flush(JSON.stringify(user, replacer));

      let expectedRpt: Reputation = new Reputation();
      expectedRpt.liked = 0;
      httpTestingController.expectOne(
        req => {
          return req.method.toUpperCase() === 'POST'
              && req.url.includes(Config.reviewRptUrl)
        },
        Config.reviewRptUrl
      ).flush(JSON.stringify(expectedRpt, replacer));

      return p;

    }).then(_ => signOut(TestBed.get(AuthService)));

  });

  it('Guest authority testcases', () => {
    expect(component).toBeTruthy();

    let user: User = User.random();
    let author: User = User.random();
    let review = Review.random(author);

    component.typeName = "Review";
    component.authorId = review.authorId;
    component.idCid = review.idCid;
    component.reputation = review.reputation;
    fixture.detectChanges();

    signOut(TestBed.get(AuthService)).then(user => {
      expect(component.user).toEqual(user);
      expect(component.canRepute()).toBeFalsy();
    });

  });

});
