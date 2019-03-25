import { TestBed } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import { LoggerModule, NgxLoggerLevel } from 'ngx-logger';

import { BackendService } from './backend.service';
import { AuthService } from './auth.service';

function setup() {
  TestBed.configureTestingModule({
    imports: [
      HttpClientModule,
      LoggerModule.forRoot({
        level: NgxLoggerLevel.TRACE,
        serverLogLevel: NgxLoggerLevel.TRACE
      })
    ],
    providers: [BackendService, AuthService]
  });
}

describe('AuthService', () => {
  beforeEach(setup);

  it('should be created', () => {
    const service: AuthService = TestBed.get(AuthService);
    expect(service).toBeTruthy();
  });
});
