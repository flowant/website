import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LoggerModule, NgxLoggerLevel, NGXLogger } from 'ngx-logger';

import { AppModule } from '../app.module';
import { AuthService, BackendService } from '../_services';
import { Config } from '../config';
import { SignUpComponent } from './sign-up.component';

describe('SignUpComponent', () => {
  let component: SignUpComponent;
  let fixture: ComponentFixture<SignUpComponent>;
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
    fixture = TestBed.createComponent(SignUpComponent);
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

  it('should valid', () => {
    component.signUpForm.patchValue({
      username: 'abcdefgh',
      email: 'a@b',
      password: 'A1bcdefg',
      confirm: 'A1bcdefg',
      accept: true
    });

    fixture.detectChanges();
    expect(component.signUpForm.valid).toBeTruthy();
  });

  it('should be invalid', () => {
    component.signUpForm.patchValue({
      username: 'abcde',
      email: 'ab',
      password: 'A1bcdefg{',
      confirm: 'A1bcdefg',
      accept: false
    });
    fixture.detectChanges();
    expect(component.f.username.valid).toBeFalsy();
    expect(component.f.username.errors.minlength).toBeTruthy();
    expect(component.f.email.valid).toBeFalsy();
    expect(component.f.email.errors.email).toBeTruthy();
    expect(component.f.password.valid).toBeFalsy();
    expect(component.f.password.errors.pattern).toBeTruthy();
    expect(component.signUpForm.errors.notSame).toBeTruthy();
    expect(component.f.accept.valid).toBeFalsy();
    expect(component.f.accept.errors.required).toBeTruthy();
  });

  it('should be invalid', () => {
    fixture.detectChanges();
    expect(component.f.username.valid).toBeFalsy();
    expect(component.f.username.errors.required).toBeTruthy();
    expect(component.f.email.valid).toBeFalsy();
    expect(component.f.email.errors.required).toBeTruthy();
    expect(component.f.password.valid).toBeFalsy();
    expect(component.f.password.errors.required).toBeTruthy();
    expect(component.signUpForm.errors).toBeFalsy()
    expect(component.f.accept.valid).toBeFalsy();
    expect(component.f.accept.errors.required).toBeTruthy();
  });

  it('Already exist username should be conflict', () => {
    component.signUpForm.patchValue({
      username: 'abcdefgh',
      email: 'a@b',
      password: 'A1bcdefg',
      confirm: 'A1bcdefg',
      accept: true
    });

    fixture.detectChanges();
    expect(component.signUpForm.valid).toBeTruthy();
    component.onSubmit().then(
      _ => {
        fixture.detectChanges();
        expect(component.error).toEqual('Conflict');
        logger.trace("signup error message:", component.error);
      }
    );

    httpTestingController.expectOne(req => {
      return req.method.toUpperCase() === 'POST'
          && req.urlWithParams.includes(Config.signupUrl);
    }).flush('user alread exist', { status: 409, statusText: 'Conflict' });

  });

});
