import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { AppModule } from '../app.module';
import { AuthService, BackendService } from '../_services';
import { Config } from '../config';
import { MessageComponent } from './message.component';
import { User, Message, replacer } from '../_models';
import { signIn, signOut } from '../_services/auth.service.spec';

describe('MessageComponent', () => {
  let component: MessageComponent;
  let fixture: ComponentFixture<MessageComponent>;
  let httpTestingController: HttpTestingController;
  let logger: NGXLogger;

  let me = User.random();
  let friend = User.random();
  let statusWithLinkHeader = {
    headers: { 'Link': '<someUrl?someParams>; rel="next"' },
    status: 200,
    statusText: 'OK'
  };

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

    // Message component requires User role.
    return signIn(me, TestBed.get(AuthService), httpTestingController).then(user => {
      fixture = TestBed.createComponent(MessageComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
      expect(component.user).toEqual(user);
      expect(component.user.isUser()).toBeTruthy();

      let rcvMsgs = [Message.fromUser(user, friend, friend.displayName + ' sent memo.')];
      httpTestingController.expectOne(
        req => {
          return req.method.toUpperCase() === 'GET'
              && req.urlWithParams.includes(Config.messageUrl + "?" + 'cid=' + user.identity);
        },
        Config.messageUrl + "?" + 'cid=' + user.identity
      ).flush(JSON.stringify(rcvMsgs, replacer));

      let sentMsgs = [Message.fromUser(friend, user, friend.displayName + ' sent memo.')];
      httpTestingController.expectOne(
        req => {
          return req.method.toUpperCase() === 'GET'
              && req.urlWithParams.includes(Config.messageUrl + "?" + 'aid=' + user.identity);
        },
        Config.messageUrl + "?" + 'aid=' + user.identity
      ).flush(JSON.stringify(sentMsgs, replacer));

    });

  });

  afterEach(() => {
    signOut(TestBed.get(AuthService)).then();
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(component.msgMap.get(component.received).length).toBeGreaterThan(0);
    expect(component.msgMap.get(component.sent).length).toBeGreaterThan(0);
    expect(component.nextInfoMap.get(component.received)).toBeFalsy();
    expect(component.nextInfoMap.get(component.sent)).toBeFalsy();
  });

  it('getNextRvc should fetch', () => {
    expect(component).toBeTruthy();
    component.isPreview = false;
    fixture.detectChanges();

    let rcvMsgs = [Message.fromUser(me, friend, friend.displayName + ' sent memo.')];
    let cntNoti = component.msgMap.get(component.received).length;

    component.getNext(component.received).then(_ => {
      fixture.detectChanges();
      expect(component.msgMap.get(component.received).length).toBe(cntNoti + rcvMsgs.length);
      rcvMsgs.map(e => expect(component.msgMap.get(component.received)).toContain(e));
      expect(component.nextInfoMap.get(component.received)).toBeTruthy();
    });

    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'GET'
            && req.urlWithParams.includes(Config.messageUrl + "?" + 'cid=' + me.identity);
      },
      Config.messageUrl + "?" + 'cid=' + me.identity
    ).flush(JSON.stringify(rcvMsgs, replacer), statusWithLinkHeader);

  });

  it('getNextSent should fetch', () => {
    expect(component).toBeTruthy();
    component.isPreview = false;
    fixture.detectChanges();

    let sentMsgs = [Message.fromUser(friend, me, me.displayName + ' sent memo.')];
    let cntNoti = component.msgMap.get(component.sent).length;

    component.getNext(component.sent).then(_ => {
      fixture.detectChanges();
      expect(component.msgMap.get(component.sent).length).toBe(cntNoti + sentMsgs.length);
      sentMsgs.map(e => expect(component.msgMap.get(component.sent)).toContain(e));
      expect(component.nextInfoMap.get(component.sent)).toBeTruthy();
    });

    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'GET'
            && req.urlWithParams.includes(Config.messageUrl + "?" + 'aid=' + me.identity);
      },
      Config.messageUrl + "?" + 'aid=' + me.identity
    ).flush(JSON.stringify(sentMsgs, replacer), statusWithLinkHeader);

  });

  it('onDelete(receive) should delete', () => {
    expect(component).toBeTruthy();
    component.isPreview = false;
    fixture.detectChanges();

    let msg: Message = component.msgMap.get(component.received)[0];

    component.onDelete(component.received, 0).then(_ => {
      fixture.detectChanges();
      expect(component.msgMap.get(component.received)).not.toContain(msg);
    });

    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'DELETE'
            && req.urlWithParams.includes(Config.messageUrl + "/" + msg.idCid.toString());
      },
      Config.messageUrl + "/" + msg.idCid.toString()
    ).flush('');

  });

  it('onDelete(sent) should delete', () => {
    expect(component).toBeTruthy();
    component.isPreview = false;
    fixture.detectChanges();

    let msg: Message = component.msgMap.get(component.sent)[0];

    component.onDelete(component.sent, 0).then(_ => {
      fixture.detectChanges();
      expect(component.msgMap.get(component.sent)).not.toContain(msg);
    });

    httpTestingController.expectOne(
      req => {
        return req.method.toUpperCase() === 'DELETE'
            && req.urlWithParams.includes(Config.messageUrl + "/" + msg.idCid.toString());
      },
      Config.messageUrl + "/" + msg.idCid.toString()
    ).flush('');

  });

});
