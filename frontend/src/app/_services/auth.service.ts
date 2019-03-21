import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { tap, concatMap } from 'rxjs/operators';
import { User, Auth } from '../_models';
import { BackendService } from './backend.service';
import { Config } from '../config';
import { NGXLogger } from 'ngx-logger';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  auth: Auth;

  constructor(
    private backendService: BackendService,
    private logger: NGXLogger) {

    this.loadAuth();
  }

  loadAuth(): Auth {
    let strAuth = localStorage.getItem(Config.AUTHENTICATION);
    this.logger.trace("load authentication from localStorage:", strAuth);

    if (strAuth) {
      this.auth = JSON.parse(strAuth);
      this.backendService.changeUser(this.auth.username).subscribe();
    } else {
      this.auth = undefined;
    }

    return this.auth;
  }

  saveAuth(auth: Auth): Auth {
    this.logger.trace("save authentication to localStorage:", auth);
    if (auth) {
      let strAuth = JSON.stringify(auth);
      localStorage.setItem(Config.AUTHENTICATION, strAuth);
    }
    this.auth = auth;
    return auth;
  }

  removeAuth(): void {
    this.logger.trace("remove authentication from localStorage");
    localStorage.removeItem(Config.AUTHENTICATION);
    this.auth = undefined;
  }

  login(username: string, password: string) {
    return this.backendService.authorize(username, password).pipe(
      tap(auth => auth.username = username),
      tap(auth => this.saveAuth(auth)),
      concatMap(_ => this.backendService.changeUser(username))
    );
  }

  logout(): Observable<User> {
    this.removeAuth();
    return this.backendService.changeUser();
  }

}

